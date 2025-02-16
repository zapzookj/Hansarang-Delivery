package com.hansarangdelivery.dto;

import com.hansarangdelivery.dto.OrderItemDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {

    @NotBlank
    private String storeName;

    @NotBlank
    private String deliveryAddress;

    @NotBlank
    private String deliveryRequest;

    @NotEmpty
    @Valid
    private List<OrderItemDto> menu;
}
