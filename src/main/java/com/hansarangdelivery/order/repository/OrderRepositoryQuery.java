package com.hansarangdelivery.order.repository;

import com.hansarangdelivery.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderRepositoryQuery {
//    Page<Order> searchOrders(Pageable pageable, String search);
//
//    Page<Order> getAllOrders(Pageable pageable);

    public Page<Order> searchByOrderId(UUID orderId, Pageable pageable);
}

