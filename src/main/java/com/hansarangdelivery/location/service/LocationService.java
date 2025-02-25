package com.hansarangdelivery.location.service;

import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.location.dto.LocationRequestDto;
import com.hansarangdelivery.location.dto.LocationResponseDto;
import com.hansarangdelivery.location.dto.RoadNameResponseDto;
import com.hansarangdelivery.location.model.Location;
import com.hansarangdelivery.global.exception.ResourceNotFoundException;
import com.hansarangdelivery.location.repository.LocationRepository;
import com.hansarangdelivery.location.repository.LocationRepositoryQuery;
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
    public PageResponseDto<LocationResponseDto> searchLocations(LocationRequestDto requestDto) {
        int page = requestDto.getPage();
        int size = requestDto.getSize();
        Pageable pageable = PageRequest.of(page-1, size);

        Page<LocationResponseDto> mappedPage =
            locationRepositoryQuery.searchLocations(pageable, requestDto).map(LocationResponseDto::new);

        return new PageResponseDto<>(mappedPage);
    }

    public boolean existsById(UUID locationId){
        return locationRepository.existsById(locationId);
    }

    public RoadNameResponseDto readRoadName(UUID locationId) {
        Location location = findLocation(locationId);
        return new RoadNameResponseDto(location.getRoadNameCode());
    }

    public LocationResponseDto readLocation(UUID locationId) {
        Location location = findLocation(locationId);
        return new LocationResponseDto(location);
    }

    private Location findLocation(UUID locationId) {
        return locationRepository.findById(locationId).orElseThrow(
            () -> new ResourceNotFoundException("해당 Id를 가진 Location 은 존재하지 않습니다.")
        );
    }
}
