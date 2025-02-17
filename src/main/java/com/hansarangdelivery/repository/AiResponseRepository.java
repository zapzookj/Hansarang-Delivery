package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.AiResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiResponseRepository extends JpaRepository<AiResponse, UUID> {
}
