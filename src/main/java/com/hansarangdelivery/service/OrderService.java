package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.OrderItemRequestDto;
import com.hansarangdelivery.dto.OrderRequestDto;
import com.hansarangdelivery.dto.OrderResponseDto;
import com.hansarangdelivery.entity.*;
import com.hansarangdelivery.entity.MenuItem;
import com.hansarangdelivery.repository.OrderRepository;
import com.hansarangdelivery.repository.OrderRepositoryQueryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final OrderRepositoryQueryImpl orderRepositoryQuery;
    private final PaymentService paymentService;

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
            OrderStatus.PENDING, // 주문 생성 되었으나 결제 전
            requestDto.getDeliveryAddress(),
            requestDto.getDeliveryRequest(),
            orderItems
        );
        orderItems.forEach(item -> item.setOrder(order));

        orderRepository.save(order);

        new OrderResponseDto(order);
    }


    public OrderResponseDto readOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        return new OrderResponseDto(order);
    }


    @Transactional
    public void updateOrder(UUID orderId, OrderRequestDto requestDto) {
        // 1. 기존 주문 찾기
        Order order = findOrder(orderId);

        paymentService.createMockPayment(orderId,order.getTotalPrice());

        // 2. 기본 정보 업데이트
        order.setDeliveryAddress(requestDto.getDeliveryAddress());
        order.setDeliveryRequest(requestDto.getDeliveryRequest());
        order.setOrderType(OrderType.valueOf(requestDto.getOrderType()));
        order.setOrderStatus(OrderStatus.valueOf(requestDto.getOrderStatus()));

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


    public Page<OrderResponseDto> searchOrders(int page, int size, String direction, String search) {
        page = Math.max(page - 1, 0); // 페이지 최소값 0부터 시작


        List<Integer> validSizes = List.of(10, 30, 50);
        if (!validSizes.contains(size)) {
            size = 10;
        }

        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;


        List<Sort.Order> orders = List.of(
            new Sort.Order(sortDirection, "createdAt"), // 1순위 정렬 (생성일)
            new Sort.Order(sortDirection, "updatedAt")  // 2순위 정렬 (수정일)
        );


        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));


        return orderRepositoryQuery.searchOrders(pageable, search)
            .map(OrderResponseDto::new);
    }








    private Order findOrder(UUID orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }


    public Page<OrderResponseDto> getAllOrders(int page, int size, String direction) {

            page = Math.max(page - 1, 0); // 페이지 최소값 0부터 시작

            Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(
                new Sort.Order(sortDirection, "createdAt"),
                new Sort.Order(sortDirection, "updatedAt")
            ));

            return orderRepositoryQuery.getAllOrders(pageable).map(OrderResponseDto::new);
        }

    }


