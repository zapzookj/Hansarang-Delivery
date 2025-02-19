package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.DeliveryAddress;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryAddressRepositoryQuery {
    void resetDefault(Long userId);
    int countByUserId(Long userId);
    Optional<DeliveryAddress> findDefaultByUserId(Long userId);
    List<DeliveryAddress> findAllByUserId(Long userId);
    Optional<DeliveryAddress> findByIdAndUserId(UUID addressId, Long userId);
    boolean existsByIdAndUserId(UUID addressId, Long userId);
}
