package com.hansarangdelivery.restaurant.dto;

import com.hansarangdelivery.restaurant.model.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "식당 응답 DTO")
public class RestaurantResponseDto {
    @Schema(description = "식당 ID", example = "식당 UUID")
    private UUID id;

    @Schema(description = "식당 이름", example = "식당 이름")
    private String name;

    @Schema(description = "식당 위치", example = "위치 UUID")
    private String location;

    @Schema(description = "식당 영업 상태", example = "true")
    private boolean status;

    @Schema(description = "식당 카테고리", example = "카테고리 UUID")
    private String category;

    @Schema(description = "식당 평점", example = "식당 평점")
    private double point;

    public RestaurantResponseDto(Restaurant restaurant,double point) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.location = restaurant.getLocation().toString();
        this.status = restaurant.getStatus();
        this.point = point;
    }

    public RestaurantResponseDto(UUID id, String name, String locationStr, boolean status, String category,double point) {
        this.id = id;
        this.name = name;
        this.location = locationStr;
        this.status = status;
        this.category = category;
        this.point = point;
    }
}
