package com.hansarangdelivery.api;

import com.hansarangdelivery.location.dto.LocationRequestDto;
import com.hansarangdelivery.location.dto.LocationResponseDto;
import com.hansarangdelivery.global.dto.ResultResponseDto;
import com.hansarangdelivery.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    // 키워드로 위치 데이터 검색 API
    @GetMapping()
    public ResultResponseDto<Page<LocationResponseDto>> searchLocations(@ModelAttribute LocationRequestDto requestDto) {
        Page<LocationResponseDto> responseDtoPage = locationService.searchLocations(requestDto);
        return new ResultResponseDto<>("조회 성공", 200, responseDtoPage);
    }
}
