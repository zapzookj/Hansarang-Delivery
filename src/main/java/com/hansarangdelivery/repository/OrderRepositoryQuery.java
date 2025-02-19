package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.Order;
import com.hansarangdelivery.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryQuery {
    Page<Order> searchOrders(Pageable pageable, String search);

    Page<Order> getAllOrders(Pageable pageable);
}

