package com.hansarangdelivery.dto;

import com.hansarangdelivery.entity.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantResponseDto {
    private String name;
    private String location;
    private boolean status;
    private String category;

    public RestaurantResponseDto(Restaurant restaurant) {
        this.name = restaurant.getName();
        this.location = restaurant.getLocation().toString();
        this.status = restaurant.getStatus();
    }

    public RestaurantResponseDto(String name, String locationStr, boolean status, String category) {
        this.name = name;
        this.location = locationStr;
        this.status = status;
        this.category = category;
    }
}
