package com.hansarangdelivery.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {
    private UUID menuId;
    private int quantity;

}
