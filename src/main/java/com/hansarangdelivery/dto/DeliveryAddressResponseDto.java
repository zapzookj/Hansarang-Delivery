package com.hansarangdelivery.dto;

import com.hansarangdelivery.entity.DeliveryAddress;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class DeliveryAddressResponseDto {
    private UUID deliveryAddressId;
    private UUID locationId;
    private String requestMessage;
    private Boolean isDefault;

    public DeliveryAddressResponseDto(DeliveryAddress deliveryAddress) {
        this.deliveryAddressId = deliveryAddress.getId();
        this.locationId = deliveryAddress.getLocationId();
        this.requestMessage = deliveryAddress.getRequestMessage();
        this.isDefault = deliveryAddress.getIsDefault();
    }

}
