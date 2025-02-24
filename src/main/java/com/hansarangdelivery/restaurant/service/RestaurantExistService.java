package com.hansarangdelivery.restaurant.service;

import com.hansarangdelivery.restaurant.model.Restaurant;
import com.hansarangdelivery.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantExistService {

    private final RestaurantRepository restaurantRepository;

    public boolean isExist(UUID restaurantId) {
        return restaurantRepository.existsById(restaurantId);
    }
}
