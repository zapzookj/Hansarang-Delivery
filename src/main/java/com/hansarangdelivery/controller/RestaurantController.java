package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.RestaurantRequestDto;
import com.hansarangdelivery.dto.RestaurantResponseDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.entity.Restaurant;
import com.hansarangdelivery.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    // 레스토랑 등록
    public ResponseEntity<ResultResponseDto> registerRestaurant(@RequestBody @Valid RestaurantRequestDto requestDto) {
        // TODO : ResultResponseDto 내용 대충 기억나는대로 한거. 나중에 올려주는거 보고 수정사항 확인하기
        restaurantService.register(requestDto);
        return ResponseEntity.status(200).body(new ResultResponseDto<>("가게 등록 성공",200));
    }
}
