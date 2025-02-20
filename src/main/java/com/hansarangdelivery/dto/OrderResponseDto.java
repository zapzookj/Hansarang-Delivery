package com.hansarangdelivery.dto;

import com.hansarangdelivery.entity.Order;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderResponseDto {
    private UUID restaurantId;
    private String storeName;
    private int totalPrice;
    private String status;
    private String roadNameCode;
    private String detailAddress;
    private String deliveryRequest;
    private List<OrderItemDto> orderItems;

    public OrderResponseDto(Order order) {
        this.restaurantId = order.getRestaurantId();
        this.storeName = order.getStoreName();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getOrderStatus().toString();
        this.roadNameCode = order.getRoadNameCode();
        this.detailAddress = order.getDetailAddress();
        this.deliveryRequest = order.getDeliveryRequest();
        this.orderItems = order.getOrderItems().stream().map(OrderItemDto::new).collect(Collectors.toList());
    }

}


