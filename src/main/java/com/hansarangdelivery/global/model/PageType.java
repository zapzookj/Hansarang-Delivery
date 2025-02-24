package com.hansarangdelivery.global.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PageType {
    TEN(10),
    THIRTY(30),
    FIFTY(50);

    private final Integer size;
}
