package com.hansarangdelivery.address.repository;

import com.hansarangdelivery.address.model.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, UUID> {
    Optional<DeliveryAddress> findByIdAndUserId(UUID addressId, Long userId);

}
