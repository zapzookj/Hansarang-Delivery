package com.hansarangdelivery.address.dto;

import com.hansarangdelivery.address.model.DeliveryAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Schema(description = "배달 주소 응답 DTO")
public class DeliveryAddressResponseDto {
    @Schema(description = "응답 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    @Schema(description = "배달 주소 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID deliveryAddressId;

    @Schema(description = "위치 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID locationId;

    @Schema(description = "배달 요청 메시지", example = "문 앞에 놓아주세요")
    private String requestMessage;

    @Schema(description = "기본 주소 여부", example = "true")
    private Boolean isDefault;

    public DeliveryAddressResponseDto(DeliveryAddress deliveryAddress) {
        this.deliveryAddressId = deliveryAddress.getId();
        this.locationId = deliveryAddress.getLocationId();
        this.requestMessage = deliveryAddress.getRequestMessage();
        this.isDefault = deliveryAddress.getIsDefault();
    }

}
