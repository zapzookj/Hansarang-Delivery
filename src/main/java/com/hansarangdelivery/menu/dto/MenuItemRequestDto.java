package com.hansarangdelivery.menu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRequestDto {

    // MenuItemId는 PathVariable 에 포함되어 있으므로 필드 선언 X
    @NotBlank(message = "메뉴 이름을 입력하세요.")
    private String name;

    @NotNull(message = "가격을 입력하세요.")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private Integer price;

    private UUID restaurantId;

    private Boolean isAvailable;
}
