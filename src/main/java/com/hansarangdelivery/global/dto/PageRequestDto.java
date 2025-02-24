package com.hansarangdelivery.global.dto;

import lombok.Getter;

@Getter
public class PageRequestDto {
    private int page;
    private int size;
    private boolean isAsc;
}
