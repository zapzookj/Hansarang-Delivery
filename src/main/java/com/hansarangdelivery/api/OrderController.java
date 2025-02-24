package com.hansarangdelivery.api;

import com.hansarangdelivery.config.PageableConfig;
import com.hansarangdelivery.order.dto.OrderRequestDto;
import com.hansarangdelivery.order.dto.OrderResponseDto;
import com.hansarangdelivery.global.dto.ResultResponseDto;
import com.hansarangdelivery.global.exception.ForbiddenActionException;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "주문 관리 API")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    @ApiResponse(responseCode = "200", description = "주문 생성 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    @PostMapping()
    public ResultResponseDto<OrderResponseDto> createOrder(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                schema = @Schema(implementation = OrderRequestDto.class),
                examples = {
                    @ExampleObject(
                        name = "주문 생성 예시",
                        value = """
                    {
                      "restaurantId": "123e4567-e89b-12d3-a456-426614174000",
                      "menuItems": [
                        {"menuId": "456e7890-e89b-12d3-a456-426614174000", "quantity": 2}
                      ]
                    }
                    """
                    )
                }
            )
        )
        @Valid @RequestBody OrderRequestDto requestDto,
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderResponseDto responseDto = orderService.createOrder(requestDto, userDetails.getUser());
        return new ResultResponseDto<>("주문 생성 성공", 200, responseDto);
    }

    @Operation(summary = "특정 주문 상세 정보 조회", description = "관리자가 특정 주문의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "특정 주문 상세 정보 조회 성공")
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음", content = @Content)
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<OrderResponseDto> readOrder(
        @Parameter(description = "주문 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable("orderId") UUID orderId) {
        OrderResponseDto responseDto = orderService.readOrder(orderId);
        return new ResultResponseDto<>("특정 주문 상세 정보 조회 성공", 200, responseDto);
    }

    @Operation(summary = "내 주문 상세 정보 조회", description = "사용자가 자신의 주문 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "내 주문 상세 정보 조회 성공")
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음", content = @Content)
    @GetMapping("/me/{orderId}")
    public ResultResponseDto<OrderResponseDto> readMyOrder(
        @Parameter(description = "주문 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable("orderId") UUID orderId,
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderResponseDto responseDto = orderService.readOrderByUser(orderId, userDetails.getUser());
        return new ResultResponseDto<>("내 주문 상세 정보 조회 성공", 200, responseDto);
    }

    @Operation(summary = "주문 수정", description = "음식점 주인이 주문 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "주문 수정 성공")
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음", content = @Content)
    @PutMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResultResponseDto<OrderResponseDto> updateOrder(
        @Parameter(description = "주문 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable("orderId") UUID orderId,
        @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto responseDto = orderService.updateOrder(orderId, requestDto);
        return new ResultResponseDto<>("주문 수정 성공", 200, responseDto);
    }

    @Operation(summary = "주문 취소", description = "사용자가 자신의 주문을 취소합니다.")
    @ApiResponse(responseCode = "200", description = "주문이 취소되었습니다.")
    @ApiResponse(responseCode = "400", description = "주문 취소 불가능합니다.", content = @Content)
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음", content = @Content)
    @DeleteMapping("/{orderId}")
    public ResultResponseDto<OrderResponseDto> deleteOrder(
        @Parameter(description = "주문 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable("orderId") UUID orderId,
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            OrderResponseDto responseDto = orderService.deleteOrder(orderId, userDetails.getUser());
            return new ResultResponseDto("주문이 취소되었습니다.", 200, responseDto);
        } catch (ForbiddenActionException e) {
            return new ResultResponseDto("주문 취소 불가능합니다.", 400);
        }
    }

    @Operation(summary = "주문 내역 조회", description = "주문 내역을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "주문 내역 조회 성공")
    @Parameters({
        @Parameter(name = "orderId", description = "주문 ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000"),
        @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
        @Parameter(name = "size", description = "페이지당 항목 수", example = "10"),
        @Parameter(name = "sort", description = "정렬 기준 (예: createdAt,desc)", example = "createdAt,desc")
    })
    @GetMapping()
    public ResultResponseDto<Page<OrderResponseDto>> searchOrder(
        @RequestParam UUID orderId,
        @Parameter(hidden = true) Pageable pageable) {
        PageableConfig.validatePageSize(pageable);
        Page<OrderResponseDto> responseList = orderService.searchOrders(orderId, pageable);
        return new ResultResponseDto<>("주문 내역 조회 성공", 200, responseList);
    }

}