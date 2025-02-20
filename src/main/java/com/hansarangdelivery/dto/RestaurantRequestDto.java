package com.hansarangdelivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class RestaurantRequestDto {
    @Length(min = 1, max = 255)
    private String name; // 가게 이름

    private UUID category_id; // 카테고리 ID

    private Long owner_id; // 가게 주인 ID

    private UUID location_id; // 위치 ID

    private boolean isOpen; // 열려있는 상태
}
