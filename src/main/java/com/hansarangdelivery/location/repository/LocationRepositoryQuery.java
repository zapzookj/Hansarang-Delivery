package com.hansarangdelivery.location.repository;

import com.hansarangdelivery.location.dto.LocationRequestDto;
import com.hansarangdelivery.location.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationRepositoryQuery {
    Page<Location> searchLocations(Pageable pageable, LocationRequestDto requestDto);
}
