package com.hansarangdelivery.repository;

import java.util.UUID;

public interface ReviewRepositoryCustom {
    Double getAverageRating(UUID restaurantId);
}
