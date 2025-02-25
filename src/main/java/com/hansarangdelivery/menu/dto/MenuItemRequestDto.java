package com.hansarangdelivery.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "메뉴 항목 요청 DTO")
public class MenuItemRequestDto {
    @Schema(description = "메뉴 이름", example = "치즈버거", required = true)
    @NotBlank(message = "메뉴 이름을 입력하세요.")
    private String name;

    @Schema(description = "메뉴 가격", example = "5000", minimum = "0", required = true)
    @NotNull(message = "가격을 입력하세요.")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private Integer price;

    @Schema(description = "레스토랑 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID restaurantId;

    @Schema(description = "메뉴 가용 여부", example = "true")
    private Boolean isAvailable;

}
