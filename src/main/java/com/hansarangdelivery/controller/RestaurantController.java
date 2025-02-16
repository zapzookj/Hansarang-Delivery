package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.RestaurantRequestDto;
import com.hansarangdelivery.dto.RestaurantResponseDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.service.RestaurantService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    // 레스토랑 등록
    public ResponseEntity<ResultResponseDto> registerRestaurant(
        @RequestBody @Valid RestaurantRequestDto requestDto) {
        // TODO : ResultResponseDto 내용 대충 기억나는대로 한거. 나중에 올려주는거 보고 수정사항 확인하기
        restaurantService.register(requestDto);
        return ResponseEntity.status(200).body(new ResultResponseDto<>("가게 등록 성공", 200));
    }

    @GetMapping("/{restaurantId}")
    // 레스토랑 단건 조회
    public ResponseEntity<ResultResponseDto<RestaurantResponseDto>> getRestaurant(
        @PathVariable UUID restaurantId) {
        RestaurantResponseDto responseDto = restaurantService.getRestaurantInfo(restaurantId);
        return ResponseEntity.ok(new ResultResponseDto<>("가게 조회 성공", 200, responseDto));
    }

    @PutMapping("/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    // 레스토랑 단건 수정
    public ResponseEntity<ResultResponseDto> updateRestaurant(
        @PathVariable UUID restaurantId,
        @RequestBody @Valid RestaurantRequestDto requestDto) {
        restaurantService.updateRestaurant(restaurantId, requestDto);
        return ResponseEntity.ok(new ResultResponseDto<>("가게 수정 성공", 200));
    }

    @DeleteMapping("/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    // 레스토랑 단건 삭제 - 삭제자와 삭제 시간 설정
    public ResponseEntity<ResultResponseDto> deleteRestaurant(@PathVariable UUID restaurantId) {
        restaurantService.deactivateRestaurant(restaurantId);
        return ResponseEntity.ok(new ResultResponseDto<>("가게 삭제 성공", 200));
    }
}
