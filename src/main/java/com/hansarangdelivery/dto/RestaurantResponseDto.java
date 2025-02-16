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

    public RestaurantResponseDto(Restaurant restaurant) {
        name = restaurant.getName();
        location = restaurant.getLocation().toString();
        status = restaurant.getStatus();
    }
}
