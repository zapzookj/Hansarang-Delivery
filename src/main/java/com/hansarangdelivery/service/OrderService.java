package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.OrderRequestDto;
import com.hansarangdelivery.dto.OrderResponseDto;
import com.hansarangdelivery.entity.*;
import com.hansarangdelivery.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public void createOrder(OrderRequestDto requestDto) {
        MenuItem menuItem = new MenuItem();
        menuItem.setPrice(1000); //테스트용
        // 1. 주문 항목 생성 및 가격 계산
        List<OrderItem> orderItems = requestDto.getMenu().stream()
            .map(menuDto -> new OrderItem(
                menuDto.getQuantity(),
                menuItem.getPrice() * menuDto.getQuantity(), // 총 가격 계산
                null
                //menuDto.getMenuId()
            ))
            .toList();
        // 2. 총 주문 가격 계산
        int totalPrice = orderItems.stream().mapToInt(OrderItem::getMenuTotalPrice).sum();

        // 3. 주문 객체 생성
        Order order = new Order(
            requestDto.getStoreName(),
            totalPrice,
            requestDto.getOrderType(),
            OrderStatus.SUCCESS, // 기본 상태 SUCCESS 설정
            requestDto.getDeliveryAddress(),
            requestDto.getDeliveryRequest(),
            orderItems
        );

        // 4. 주문 항목을 주문과 연결 (OrderItem의 order 필드 설정)
        orderItems.forEach(item -> item.setOrder(order));

        // 5. 주문 저장 (OrderItem도 Cascade.ALL로 인해 자동 저장됨)
        orderRepository.save(order);
    }


    public OrderResponseDto getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        return new OrderResponseDto(order);
    }
}

