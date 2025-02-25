package com.hansarangdelivery.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING("주문 생성"),
    PAID("결제 완료"),
    PROCESSING("조리 중"),
    DELIVERED("배송 중"),
    COMPLETED("배송 완료"),
    CANCELED("주문 취소");

    private final String description;


}
