package com.hansarangdelivery.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    @NotNull
    private UUID menuId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
