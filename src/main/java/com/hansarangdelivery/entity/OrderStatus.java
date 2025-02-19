package com.hansarangdelivery.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING("주문 생성"),
    PAID("결제완료"),

    SUCCESS("주문 성공"),
    CANCELED("주문 취소");

    private final String description;


}
