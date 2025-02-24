package com.hansarangdelivery.restaurant.dto;

import com.hansarangdelivery.restaurant.model.Restaurant;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RestaurantResponseDto {
    private UUID id;
    private String name;
    private String location;
    private boolean status;
    private String category;
    private double point;

    public RestaurantResponseDto(Restaurant restaurant,double point) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.location = restaurant.getLocation().toString();
        this.status = restaurant.getStatus();
        this.category = restaurant.getCategory().toString();
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
