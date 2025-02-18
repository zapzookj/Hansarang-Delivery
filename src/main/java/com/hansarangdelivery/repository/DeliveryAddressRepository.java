package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.DeliveryAddress;
import com.hansarangdelivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, UUID> {
    int countByUser(User user);

    @Modifying
    @Query("UPDATE DeliveryAddress d SET d.isDefault = false WHERE d.user = :user and d.isDefault = true")
    void resetDefault(@Param("user") User user);
}
