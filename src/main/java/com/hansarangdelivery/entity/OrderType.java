package com.hansarangdelivery.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum OrderType {
    ONLINE("온라인 주문"),
    OFFLINE("대면 주문");

    private final String description;

}