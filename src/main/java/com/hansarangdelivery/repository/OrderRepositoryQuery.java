package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.Order;
import com.hansarangdelivery.entity.OrderStatus;
import com.hansarangdelivery.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderRepositoryQuery {
//    Page<Order> searchOrders(Pageable pageable, String search);
//
//    Page<Order> getAllOrders(Pageable pageable);

    public Page<Order> searchByOrderId(UUID orderId, Pageable pageable);
}

