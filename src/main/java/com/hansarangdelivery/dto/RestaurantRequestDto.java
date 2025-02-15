package com.hansarangdelivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantRequestDto {
    @NotBlank
    @Pattern(
        regexp = "^\\d{10}$",
        message = "사업자 등록번호는 10자리 숫자여야 합니다."
    )
    private String name; // 가게 이름

    @NotNull
    private UUID category_id; // 카테고리 ID

    @NotNull
    private UUID owner_id; // 가게 주인 ID

    @NotNull
    private UUID location_id; // 위치 ID
}
