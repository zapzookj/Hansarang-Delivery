package com.hansarangdelivery.order.dto;

import com.hansarangdelivery.order.model.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "주문 항목 DTO")
public class OrderItemDto {
    @Schema(description = "메뉴 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID menuId;
    @Schema(description = "메뉴 이름", example = "치즈버거")
    private String menuName;

    @Schema(description = "메뉴 단가", example = "5000")
    private int menuPrice;

    @Schema(description = "주문 수량", example = "2")
    private int quantity;

    @Schema(description = "메뉴 총 가격", example = "10000")
    private int menuTotalPrice;

    public OrderItemDto(OrderItem orderItem) {
        this.menuId = orderItem.getMenuId();
        this.menuName = orderItem.getMenuName();
        this.menuPrice = orderItem.getMenuPrice();
        this.quantity = orderItem.getQuantity();
        this.menuTotalPrice = orderItem.getMenuTotalPrice();
    }

}
