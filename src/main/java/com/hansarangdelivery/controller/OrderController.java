package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.OrderRequestDto;
import com.hansarangdelivery.dto.OrderResponseDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.entity.Order;
import com.hansarangdelivery.exception.ForbiddenActionException;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;




@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping //주문 생성
    public ResultResponseDto<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderResponseDto order = orderService.createOrder(requestDto, userDetails.getUser());// 주문 생성 로직 실행
        return new ResultResponseDto<>("주문 생성 성공", 200,order);
    }


    @GetMapping("/{orderId}") // 특정 주문 상세 정보 조회
    public ResultResponseDto<OrderResponseDto> readOrder(@PathVariable("orderId") UUID orderId) {
        OrderResponseDto responseDto = orderService.readOrder(orderId);
        return new ResultResponseDto<>("특정 주문 상세 정보 조회 성공", 200, responseDto);
    }

    @PutMapping("/{orderId}")  //특정 주문 수정 (오너만)
    public ResultResponseDto<Order> updateOrder(@PathVariable("orderId") UUID orderId, @RequestBody OrderRequestDto requestDto) {
        Order order = orderService.updateOrder(orderId, requestDto);
        return new ResultResponseDto<>("주문 수정 성공", 200,order);

    }

    @DeleteMapping("{orderId}")
    public ResultResponseDto<Order> updateOrder(@PathVariable("orderId") UUID orderId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Order order = orderService.deleteOrder(orderId, userDetails.getUser());
            return new ResultResponseDto("주문이 취소되었습니다.", 200,order);
        } catch (ForbiddenActionException e) {
            return new ResultResponseDto("주문 취소 불가능합니다.", 400);
        }
    }

//    @GetMapping("/")
//    public ResultResponseDto<Page<OrderResponseDto>> searchOrder(
//        @RequestParam(defaultValue = "1") int page,
//        @RequestParam(defaultValue = "10") int size,
//        @RequestParam(defaultValue = "desc") String direction,
//        @RequestParam(required = false) String search) {
//
//        Page<OrderResponseDto> orders = orderService.searchOrders(page, size, direction, search);
//        return new ResultResponseDto<>("주문 검색 성공", 200, orders);
//    }
//
//
//    @GetMapping("/searchAll")
//    public ResultResponseDto<Page<OrderResponseDto>> getAllOrders(
//        @RequestParam(defaultValue = "1") int page,
//        @RequestParam(defaultValue = "10") int size,
//        @RequestParam(defaultValue = "desc") String direction) {
//
//        Page<OrderResponseDto> orders = orderService.getAllOrders(page, size, direction);
//        return new ResultResponseDto<>("주문 목록 조회 성공", 200, orders);
//    }


}