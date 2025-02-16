package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.OrderRequestDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ResultResponseDto<Void>> createOrder(@Valid @RequestBody OrderRequestDto requestDto) {
        orderService.createOrder(requestDto);  // 주문 생성 로직 실행
        return ResponseEntity.ok(new ResultResponseDto<>("주문 생성 성공", 200));
    }


}