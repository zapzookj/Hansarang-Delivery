package com.hansarangdelivery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hansarangdelivery.dto.OrderItemDto;
import com.hansarangdelivery.entity.OrderType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {

    @NotBlank
    private String storeName;

    @NotNull
    private OrderType orderType;

    @NotBlank
    private String deliveryAddress;

    @NotBlank
    private String deliveryRequest;

    @NotEmpty
    @Valid
    private List<OrderItemDto> menu;
}
