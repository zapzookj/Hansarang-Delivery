package com.hansarangdelivery.entity;

public enum OrderStatus {
    SUCCESS("주문 성공"),
    FAILURE("주문 실패");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
