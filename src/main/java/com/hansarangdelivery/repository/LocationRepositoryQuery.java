package com.hansarangdelivery.repository;

import com.hansarangdelivery.dto.LocationRequestDto;
import com.hansarangdelivery.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationRepositoryQuery {
    Page<Location> searchLocations(Pageable pageable, LocationRequestDto requestDto);
}
