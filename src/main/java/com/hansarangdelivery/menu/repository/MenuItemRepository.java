package com.hansarangdelivery.menu.repository;

import com.hansarangdelivery.menu.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, UUID>, MenuItemRepositoryQuery {

    List<MenuItem> findAllByRestaurantId(UUID restaurantId);

    boolean existsByRestaurantIdAndName(UUID restaurantId, String name);

    boolean existsByRestaurantId(UUID restaurantId);
}
