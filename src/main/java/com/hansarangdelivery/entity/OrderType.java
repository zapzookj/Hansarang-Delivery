package com.hansarangdelivery.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
public enum OrderType {
    ONLINE("온라인 주문"),
    OFFLINE("대면 주문");

    private final String description;

}

