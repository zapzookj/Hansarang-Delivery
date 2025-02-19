package com.hansarangdelivery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class DeliveryAddressRequestDto {
    private UUID locationId;

    @Size(max = 20)
    private String requestMessage;
    private Boolean isDefault;
}
