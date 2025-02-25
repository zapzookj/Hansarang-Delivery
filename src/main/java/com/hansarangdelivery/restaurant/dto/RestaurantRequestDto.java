package com.hansarangdelivery.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Schema(description = "음식점 등록/수정 요청 DTO")
public class RestaurantRequestDto {
    @Schema(description = "가게 이름", example = "맛있는 식당", minLength = 1, maxLength = 255)
    @Length(min = 1, max = 255)
    private String name;

    @Schema(description = "카테고리 ID", example = "카테고리의 UUID")
    private UUID category_id;

    @Schema(description = "가게 주인 ID", example = "가게 주인의 ID")
    private Long owner_id;

    @Schema(description = "위치 ID", example = "가게 위치 UUID")
    private UUID location_id;

    @Schema(description = "영업 상태", example = "열렸으면:true,닫혔으면:false")
    private boolean isOpen;
}
