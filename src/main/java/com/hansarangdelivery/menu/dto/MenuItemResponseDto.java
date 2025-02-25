package com.hansarangdelivery.menu.dto;

import com.hansarangdelivery.menu.model.MenuItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "메뉴 항목 응답 DTO")
@AllArgsConstructor
public class MenuItemResponseDto {
    // 메뉴를 조회할 때 Restaurant 페이지에서 조회하는 것을 고려하여
    // Restaurant 관련 정보는 응답 X
    @Schema(description = "메뉴 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "메뉴 이름", example = "치즈버거")
    private String name;

    @Schema(description = "메뉴 가격", example = "5000")
    private Integer price;

    @Schema(description = "메뉴 가용 여부", example = "true")
    private boolean isAvailable;

    public MenuItemResponseDto(MenuItem menuItem) {
        this.id = menuItem.getId();
        this.name = menuItem.getName();
        this.price = menuItem.getPrice();
        this.isAvailable = menuItem.getIsAvailable();
    }

}
