package com.hansarangdelivery.api;

import com.hansarangdelivery.location.dto.LocationRequestDto;
import com.hansarangdelivery.location.dto.LocationResponseDto;
import com.hansarangdelivery.global.dto.ResultResponseDto;
import com.hansarangdelivery.location.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService locationService;

    @Operation(summary = "위치 데이터 검색", description = "키워드로 위치 데이터를 검색합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    @GetMapping()
    public ResultResponseDto<Page<LocationResponseDto>> searchLocations(
        @Parameter(description = "위치 검색 요청 DTO")
        @ModelAttribute LocationRequestDto requestDto) {
        Page<LocationResponseDto> responseDtoPage = locationService.searchLocations(requestDto);
        return new ResultResponseDto<>("조회 성공", 200, responseDtoPage);
    }

}
