package com.hansarangdelivery.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderRequestDto {
    private Long userId;
    private UUID restaurantId;
    private String roadName;
    private String detailAddress;
    private String deliveryRequest;
    private String orderType;
    private String orderStatus;
    private List<OrderItemRequestDto> menu;
}
