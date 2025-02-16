package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.MenuItem;
import com.hansarangdelivery.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
    Optional<List<MenuItem>> findAllByRestaurant(Restaurant restaurant);
}
