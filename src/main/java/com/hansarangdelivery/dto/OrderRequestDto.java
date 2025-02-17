package com.hansarangdelivery.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderRequestDto {
    private Long userId;
    private UUID storeId;
    private String deliveryAddress;
    private String deliveryRequest;
    private String orderType;  // ENUM 문자열 값으로 받기
    private List<OrderItemRequestDto> menu;  // ✅ OrderItemDto 사용하여 메뉴 정보 포함
}
