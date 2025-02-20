package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.RestaurantRequestDto;
import com.hansarangdelivery.dto.RestaurantResponseDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.service.RestaurantService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    // 레스토랑 등록
    public ResultResponseDto<RestaurantResponseDto> createRestaurant(
        @RequestBody @Valid RestaurantRequestDto requestDto) {
        RestaurantResponseDto response = restaurantService.register(requestDto);
        return new ResultResponseDto<>("가게 등록 성공", 200,response);
    }

    @GetMapping("/{restaurantId}")
    // 레스토랑 단건 조회
    public ResultResponseDto<RestaurantResponseDto> readRestaurant(
        @PathVariable UUID restaurantId) {
        RestaurantResponseDto responseDto = restaurantService.getRestaurantInfo(restaurantId);
        return new ResultResponseDto<>("가게 조회 성공", 200, responseDto);
    }

    @PutMapping("/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_OWNER')")
    // 레스토랑 단건 수정
    public ResultResponseDto<RestaurantResponseDto> updateRestaurant(
        @PathVariable UUID restaurantId,
        @RequestBody @Valid RestaurantRequestDto requestDto) {
        RestaurantResponseDto result = restaurantService.updateRestaurant(restaurantId, requestDto);
        return new ResultResponseDto<>("가게 수정 성공", 200,result);
    }

    @DeleteMapping("/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    // 레스토랑 단건 삭제 - 삭제자와 삭제 시간 설정
    public ResultResponseDto<RestaurantResponseDto> deleteRestaurant(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable UUID restaurantId) {
        RestaurantResponseDto result = restaurantService.deleteRestaurant(userDetails.getUser(),restaurantId);
        return new ResultResponseDto<>("가게 삭제 성공", 200,result);
    }

    @GetMapping("/search")
    public ResultResponseDto<Page<RestaurantResponseDto>> searchRestaurants(
    // 레스토랑 리스트 검색, search가 포함된 음식점을 찾는데 사용될 예정
        Pageable pageable,
        @RequestParam(required = false) String search) {
        Page<RestaurantResponseDto> restaurants = restaurantService.searchRestaurants(pageable,search);
        return new ResultResponseDto<>("가게 검색 성공", 200, restaurants);
    }
}
