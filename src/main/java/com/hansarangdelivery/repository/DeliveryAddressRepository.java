package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.DeliveryAddress;
import com.hansarangdelivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, UUID> {
    @Modifying
    @Query("UPDATE DeliveryAddress d SET d.isDefault = false WHERE d.user.id = :userId and d.isDefault = true")
    void resetDefault(@Param("userId") Long userId);

    int countByUserId(Long userId);

    Optional<DeliveryAddress> findByUserIdAndDefaultIsTrue(Long userId);

    List<DeliveryAddress> findAllByUserId(Long userId);

    Optional<DeliveryAddress> findByIdAndUserId(UUID addressId, Long userId);

    boolean existsByIdAndUserId(UUID addressId, Long userId);
}
