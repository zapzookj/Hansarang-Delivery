package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MenuItemRepositoryQuery {

    Page<MenuItem> searchMenuItemByRestaurantId(UUID restaurantId, Pageable pageable);
}
