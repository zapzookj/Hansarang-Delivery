package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, UUID> {
}
