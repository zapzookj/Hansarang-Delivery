package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.LocationRequestDto;
import com.hansarangdelivery.dto.LocationResponseDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    @GetMapping("")
    public ResponseEntity<ResultResponseDto<Page<LocationResponseDto>>> searchLocations(@ModelAttribute LocationRequestDto requestDto) {
        Page<LocationResponseDto> responseDtoPage = locationService.searchLocations(requestDto);
        return ResponseEntity.status(200).body(new ResultResponseDto<>("조회 성공", 200, responseDtoPage));
    }
}
