package com.hansarangdelivery.dto;

import com.hansarangdelivery.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class OrderItemDto {
    private UUID orderItemId;
    private UUID menuId;
    private String menuName;
    private int menuPrice;
    private int quantity;
    private int menuTotalPrice;

    public OrderItemDto(OrderItem orderItem) {
        this.menuId = orderItem.getMenuId();
        this.menuName = orderItem.getMenuName();
        this.menuPrice = orderItem.getMenuPrice();
        this.quantity = orderItem.getQuantity();
        this.menuTotalPrice = orderItem.getMenuTotalPrice();
    }
}
