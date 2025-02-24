package com.hansarangdelivery.location.dto;

import com.hansarangdelivery.location.model.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "위치 응답 DTO")
public class LocationResponseDto {
    @Schema(description = "위치 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    @Schema(description = "시/도", example = "서울특별시")
    private String city;

    @Schema(description = "시군구", example = "강남구")
    private String district;

    @Schema(description = "읍면동", example = "역삼동")
    private String subDistrict;

    @Schema(description = "주 지번", example = "123")
    private String mainLotNumber;

    @Schema(description = "부 지번", example = "45")
    private String subLotNumber;

    @Schema(description = "도로명 코드", example = "12345678")
    private String roadNameCode;

    @Schema(description = "건물 본번", example = "67")
    private String buildingMainNumber;

    @Schema(description = "건물 부번", example = "89")
    private String buildingSubNumber;

    public LocationResponseDto(Location location) {
        this.id = location.getId();
        this.city = location.getCity();
        this.district = location.getDistrict();
        this.subDistrict = location.getSubDistrict();
        this.mainLotNumber = location.getMainLotNumber();
        this.subLotNumber = location.getSubLotNumber();
        this.roadNameCode = location.getRoadNameCode();
        this.buildingMainNumber = location.getBuildingMainNumber();
        this.buildingSubNumber = location.getBuildingSubNumber();
    }

}
