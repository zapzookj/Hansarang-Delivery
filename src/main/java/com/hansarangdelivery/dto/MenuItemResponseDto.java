package com.hansarangdelivery.dto;

import com.hansarangdelivery.entity.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponseDto {

    // 메뉴를 조회할 때 Restaurant 페이지에서 조회하는 것을 고려하여
    // Restaurant 관련 정보는 응답 X

    private String name;

    private Integer price;

    private boolean isAvailable;

    public MenuItemResponseDto(MenuItem menuItem) {
        this.name = menuItem.getName();
        this.price = menuItem.getPrice();
        this.isAvailable = menuItem.getIsAvailable();
    }
}
