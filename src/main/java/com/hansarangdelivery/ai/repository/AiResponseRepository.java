package com.hansarangdelivery.ai.repository;

import com.hansarangdelivery.ai.model.AiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiResponseRepository extends JpaRepository<AiResponse, UUID> {
    Page<AiResponse> findByUserId(Long userId, Pageable pageable);
}
