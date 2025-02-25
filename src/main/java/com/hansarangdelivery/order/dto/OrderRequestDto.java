package com.hansarangdelivery.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "주문 요청 DTO")
public class OrderRequestDto {
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;
    @Schema(description = "레스토랑 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID restaurantId;

    @Schema(description = "도로명 주소", example = "서울특별시 강남구 테헤란로 123")
    private String roadName;

    @Schema(description = "상세 주소", example = "456동 789호")
    private String detailAddress;

    @Schema(description = "배달 요청사항", example = "문 앞에 놓아주세요.")
    private String deliveryRequest;

    @Schema(description = "주문 유형", example = "DELIVERY", allowableValues = {"DELIVERY", "PICKUP"})
    private String orderType;

    @Schema(description = "주문 상태", example = "PENDING", allowableValues = {"PENDING", "ACCEPTED", "PREPARING", "DELIVERING", "COMPLETED", "CANCELLED"})
    private String orderStatus;

    @Schema(description = "주문 메뉴 목록")
    private List<OrderItemRequestDto> menu;

}
