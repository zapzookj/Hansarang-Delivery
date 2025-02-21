package com.hansarangdelivery.dto;

import com.hansarangdelivery.entity.Restaurant;
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

    public RestaurantResponseDto(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.location = restaurant.getLocation().toString();
        this.status = restaurant.getStatus();
    }

    public RestaurantResponseDto(UUID id, String name, String locationStr, boolean status, String category) {
        this.id = id;
        this.name = name;
        this.location = locationStr;
        this.status = status;
        this.category = category;
    }
}
