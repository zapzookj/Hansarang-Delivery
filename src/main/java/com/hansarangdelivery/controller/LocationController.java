package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.LocationRequestDto;
import com.hansarangdelivery.dto.LocationResponseDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.dto.RoadNameResponseDto;
import com.hansarangdelivery.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    // 키워드로 위치 데이터 검색 API
    @GetMapping("")
    public ResultResponseDto<Page<LocationResponseDto>> searchLocations(@ModelAttribute LocationRequestDto requestDto) {
        Page<LocationResponseDto> responseDtoPage = locationService.searchLocations(requestDto);
        return new ResultResponseDto<>("조회 성공", 200, responseDtoPage);
    }
}
