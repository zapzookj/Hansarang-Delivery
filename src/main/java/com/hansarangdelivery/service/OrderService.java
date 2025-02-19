package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.OrderItemRequestDto;
import com.hansarangdelivery.dto.OrderRequestDto;
import com.hansarangdelivery.dto.OrderResponseDto;
import com.hansarangdelivery.entity.*;
import com.hansarangdelivery.entity.MenuItem;
import com.hansarangdelivery.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemService menuItemService;
    private final RestaurantService restaurantService; //  가게 정보 조회

    @Transactional
    public void createOrder(OrderRequestDto requestDto,User user) {

        Restaurant restaurant = restaurantService.getRestaurantById(requestDto.getRestaurantId());

        String storeName = restaurant.getName();

        List<OrderItem> orderItems = requestDto.getMenu().stream().map(orderItemDto -> {

            MenuItem menuItem = menuItemService.getMenuById(orderItemDto.getMenuId());

            return new OrderItem(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getPrice(),
                orderItemDto.getQuantity(),
                null
            );
        }).collect(Collectors.toList());

        // 주문 총 가격 계산
        int totalPrice = orderItems.stream()
            .mapToInt(OrderItem::getMenuTotalPrice)
            .sum();

        // 주문 객체 생성
        Order order = new Order(
            user.getId(),
            requestDto.getRestaurantId(),
            storeName, //RestaurantRepository 에서 가져온 storeName 사용
            totalPrice,
            OrderType.valueOf(requestDto.getOrderType()),
            OrderStatus.SUCCESS, // 주문 성공 상태로 변경
            requestDto.getDeliveryAddress(),
            requestDto.getDeliveryRequest(),
            orderItems
        );
        orderItems.forEach(item -> item.setOrder(order));

        orderRepository.save(order);

        new OrderResponseDto(order);
    }


    public OrderResponseDto getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        return new OrderResponseDto(order);
    }


    @Transactional
    public void updateOrder(UUID orderId, OrderRequestDto requestDto) {
        // 1. 기존 주문 찾기
        Order order = findOrder(orderId);

        // 2. 기본 정보 업데이트
        order.setDeliveryAddress(requestDto.getDeliveryAddress());
        order.setDeliveryRequest(requestDto.getDeliveryRequest());
        order.setOrderType(OrderType.valueOf(requestDto.getOrderType()));

        // 3. 기존 OrderItem 목록을 삭제하기 전에 부모 연관 관계를 해제
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(null));
        order.getOrderItems().clear();
        orderRepository.save(order);

        // 4. 새로운 OrderItem 추가
        List<OrderItem> newOrderItems = new ArrayList<>();
        int totalPrice = 0;

        for (OrderItemRequestDto itemDto : requestDto.getMenu()) {
            MenuItem menu = menuItemService.getMenuById(itemDto.getMenuId());

            OrderItem newItem = new OrderItem(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                itemDto.getQuantity(),
                order
            );
            newOrderItems.add(newItem);
            totalPrice += newItem.getMenuTotalPrice();
        }

        // 5. 새로운 OrderItem 목록 설정
        order.getOrderItems().addAll(newOrderItems);

        // 6. 총 주문 금액 업데이트 후 저장
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(UUID orderId, User user) {
        Order order = findOrder(orderId);

        // 주문이 생성된 시각
        LocalDateTime createdAt = order.getCreatedAt();

        // 현재 시각
        LocalDateTime now = LocalDateTime.now();

        // 주문이 5분이 초과되었는지 검사
        if (createdAt.plusMinutes(5).isBefore(now)) {
            throw new IllegalArgumentException();
        }

        // 주문 취소 처리
        order.delete(now, user.getId().toString());

        // 주문 상태 변경 (예: CANCELED)
        order.setOrderStatus(OrderStatus.CANCELED);


        // 변경 사항 저장
        orderRepository.save(order);
    }






    private Order findOrder(UUID orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }


}

