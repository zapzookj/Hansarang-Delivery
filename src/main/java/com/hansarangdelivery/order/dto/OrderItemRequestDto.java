package com.hansarangdelivery.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주문 항목 요청 DTO")
public class OrderItemRequestDto {
    @Schema(description = "메뉴 ID", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID menuId;
    @Schema(description = "주문 수량", example = "2", minimum = "1", required = true)
    private int quantity;
}
