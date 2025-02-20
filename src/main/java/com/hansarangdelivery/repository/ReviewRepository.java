package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID>, ReviewRepositoryCustom {
    Boolean existsByOrderId(UUID orderId);

    Page<Review> findAllByCreatedBy(String createdBy, Pageable pageable);

    Page<Review> findAllByRestaurantId(UUID restaurantId, Pageable pageable);
}

