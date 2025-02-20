package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.LocationRequestDto;
import com.hansarangdelivery.dto.LocationResponseDto;
import com.hansarangdelivery.entity.Location;
import com.hansarangdelivery.exception.ResourceNotFoundException;
import com.hansarangdelivery.repository.LocationRepository;
import com.hansarangdelivery.repository.LocationRepositoryQuery;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final LocationRepositoryQuery locationRepositoryQuery;
    public Page<LocationResponseDto> searchLocations(LocationRequestDto requestDto) {
        int page = requestDto.getPage();
        int size = requestDto.getSize();
        Pageable pageable = PageRequest.of(page-1, size);

        return locationRepositoryQuery.searchLocations(pageable, requestDto).map(LocationResponseDto::new);
    }

    public boolean existsById(UUID locationId){
        return locationRepository.existsById(locationId);
    }
}
