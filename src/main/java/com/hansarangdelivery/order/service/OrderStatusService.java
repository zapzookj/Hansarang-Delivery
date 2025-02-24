package com.hansarangdelivery.order.service;

import com.hansarangdelivery.order.model.Order;
import com.hansarangdelivery.order.model.OrderStatus;
import com.hansarangdelivery.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderStatusService {

    private final OrderRepository orderRepository;

    public boolean isCompleted(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            return order.getOrderStatus().equals(OrderStatus.COMPLETED);
        }
        return false;
    }
}
