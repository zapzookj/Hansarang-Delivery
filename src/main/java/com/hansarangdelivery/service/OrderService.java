package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.OrderRequestDto;
import com.hansarangdelivery.dto.OrderResponseDto;
import com.hansarangdelivery.entity.*;
import com.hansarangdelivery.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void createOrder(OrderRequestDto requestDto) {
        Long userId = requestDto.getUserId();

        Restaurant restaurant = restaurantService.getRestaurantById(requestDto.getStoreId());

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
            userId,
            requestDto.getStoreId(),
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
}

//    public void updateOrder(UUID orderId, OrderRequestDto requestDto) {
//        // 1. 주문 객체 찾기
//        Order order = findOrder(orderId);
//
//        // 2. 주문 정보 업데이트
//        updateOrderDetails(order, requestDto);
//
//        // 3. 메뉴 항목 업데이트
//        List<OrderItem> updatedItems = convertToOrderItems(requestDto.getMenu(), order);
//
//        // 4. 주문에 메뉴 항목 설정
//        order.setOrderItems(updatedItems);
//
//        // 5. 변경된 주문 객체 저장
//        orderRepository.save(order);
//    }

//    private void updateOrderDetails(Order order, OrderRequestDto requestDto) {
//        order.setOrderType(requestDto.getOrderType());
//        order.setDeliveryAddress(requestDto.getDeliveryAddress());
//        order.setDeliveryRequest(requestDto.getDeliveryRequest());
//    }
//
//
//
//    private List<OrderItem> convertToOrderItems(List<OrderItemDto> menuItems, Order order) {
//        return menuItems.stream()
//            .map(itemDto -> new OrderItem(itemDto.getQuantity(), calculateMenuTotalPrice(itemDto), order)) // 메뉴 가격 계산
//            .collect(Collectors.toList());
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//    private Order findOrder(UUID orderId) {
//        return orderRepository.findById(orderId)
//            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
//    }
//}

