package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.OrderRequestDto;
import com.hansarangdelivery.dto.OrderResponseDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping //주문 생성
    public ResponseEntity<ResultResponseDto<Void>> createOrder(@Valid @RequestBody OrderRequestDto requestDto) {
        orderService.createOrder(requestDto);  // 주문 생성 로직 실행
        return ResponseEntity.ok(new ResultResponseDto<>("주문 생성 성공", 200));
    }


    @GetMapping("/{orderId}") // 특정 주문 상세 정보 조회
    public ResponseEntity<ResultResponseDto<OrderResponseDto>> getOrder(@PathVariable("orderId") UUID orderId) {
        OrderResponseDto responseDto = orderService.getOrder(orderId);
        return ResponseEntity.status(200).body(new ResultResponseDto<>("특정 주문 상세 정보 조회 성공", 200, responseDto));
    }

    @PutMapping("/{orderId}")  //특정 주문 수정 (오너만)
    public ResponseEntity<ResultResponseDto<Void>> updateOrder(@PathVariable("orderId") UUID orderId,@RequestBody OrderRequestDto requestDto){
        orderService.updateOrder(orderId,requestDto);
        return ResponseEntity.ok(new ResultResponseDto<>("주문 수정 성공", 200));


    }


}