package com.hansarangdelivery.order.dto;

import com.hansarangdelivery.order.model.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Schema(description = "주문 응답 DTO")
public class OrderResponseDto {
    @Schema(description = "주문 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    @Schema(description = "레스토랑 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID restaurantId;

    @Schema(description = "가게 이름", example = "맛있는 식당")
    private String storeName;

    @Schema(description = "총 주문 금액", example = "25000")
    private int totalPrice;

    @Schema(description = "주문 상태", example = "PENDING", allowableValues = {"PENDING", "ACCEPTED", "PREPARING", "DELIVERING", "COMPLETED", "CANCELLED"})
    private String status;

    @Schema(description = "도로명 주소 코드", example = "12345")
    private String roadNameCode;

    @Schema(description = "상세 주소", example = "456동 789호")
    private String detailAddress;

    @Schema(description = "배달 요청사항", example = "문 앞에 놓아주세요.")
    private String deliveryRequest;

    @Schema(description = "주문 항목 목록")
    private List<OrderItemDto> orderItems;

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.restaurantId = order.getRestaurantId();
        this.storeName = order.getStoreName();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getOrderStatus().toString();
        this.roadNameCode = order.getRoadNameCode();
        this.detailAddress = order.getDetailAddress();
        this.deliveryRequest = order.getDeliveryRequest();
        this.orderItems = order.getOrderItems().stream().map(OrderItemDto::new).collect(Collectors.toList());
    }
}


