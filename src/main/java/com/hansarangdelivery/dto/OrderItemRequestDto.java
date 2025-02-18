package com.hansarangdelivery.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class OrderItemRequestDto {
    private UUID menuId;
    private int quantity;
}

