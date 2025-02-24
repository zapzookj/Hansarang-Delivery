package com.hansarangdelivery.api;

import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.location.dto.LocationRequestDto;
import com.hansarangdelivery.location.dto.LocationResponseDto;
import com.hansarangdelivery.global.dto.ResultResponseDto;
import com.hansarangdelivery.location.model.LocationDocument;
import com.hansarangdelivery.location.service.LocationSearchService;
import com.hansarangdelivery.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;
    private final LocationSearchService locationSearchService;

    // 키워드로 위치 데이터 검색 API (RDB 기반)
    @GetMapping()
    public ResultResponseDto<PageResponseDto<LocationResponseDto>> searchLocations(@ModelAttribute LocationRequestDto requestDto) {
        PageResponseDto<LocationResponseDto> responseDtoPage = locationService.searchLocations(requestDto);
        return new ResultResponseDto<>("조회 성공", 200, responseDtoPage);
    }

    @GetMapping("/search")
    public ResultResponseDto<PageResponseDto<LocationDocument>> searchLocationsAsEs(@ModelAttribute LocationRequestDto requestDto) {
        PageResponseDto<LocationDocument> responseDtoPage = locationSearchService.searchLocationAsEs(requestDto);
        return new ResultResponseDto<>("조회 성공", 200, responseDtoPage);
    }
}
