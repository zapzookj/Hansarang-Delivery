package com.hansarangdelivery.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MenuItemRequestDto {

    // MenuItemId는 PathVariable 에 포함되어 있으므로 필드 선언 X
    // Update 시 Null 판단을 위해 Object 형식으로 선언
    private String name;

    private Integer price;

    private Boolean isAvailable;

    private UUID restaurantId;
}
