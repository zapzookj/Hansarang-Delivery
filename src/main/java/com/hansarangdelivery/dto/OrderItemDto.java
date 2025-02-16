package com.hansarangdelivery.dto;

import com.hansarangdelivery.entity.OrderItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemDto {

    @NotNull
    private UUID menuId;

    @NotNull
    @Min(1)
    private Integer quantity;

    public OrderItemDto(OrderItem orderItem) {
        this.menuId = orderItem.getId();
        this.quantity = orderItem.getQuantity();

    }
}
