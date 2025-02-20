package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReviewRepositoryCustom {

    Double getAverageRating(UUID restaurantId);

    Page<Review> searchByRestaurantId(UUID restaurantId, Pageable pageable);

    Page<Review> searchByUserId(String userId, Pageable pageable);
}
