package com.hansarangdelivery.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    SUCCESS("주문 성공"),
    FAILURE("주문 실패");

    private final String description;


}
