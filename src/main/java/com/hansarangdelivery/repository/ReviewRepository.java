package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Boolean existsByOrderId(UUID orderId);

    Optional<List<Review>> findAllByCreatedBy(UUID userId);
}

