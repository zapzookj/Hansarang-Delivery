package com.hansarangdelivery.dto;

import com.hansarangdelivery.entity.Order;
import com.hansarangdelivery.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderResponseDto {

    private String storeName;

    private int  totalPrice;

    private String status;

    private String deliveryAddress;

    private String deliveryRequest;

    private List<OrderItemDto> orderItems;

    public OrderResponseDto(Order order) {
        this.storeName = order.getStoreName();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus().toString();
        this.deliveryAddress = order.getDeliveryAddress();
        this.deliveryRequest = order.getDeliveryRequest();
        this.orderItems = order.getOrderItems().stream().map(OrderItemDto::new).collect(Collectors.toList());
    }

}





/*
@Getter
@Setter
public class UserResponseDto {
    private String username;
    private String email;
    private UserRole role;

    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}

 */