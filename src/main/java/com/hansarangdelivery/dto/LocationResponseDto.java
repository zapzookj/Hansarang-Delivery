package com.hansarangdelivery.dto;

import com.hansarangdelivery.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponseDto {
    private UUID id;
    private String city;
    private String district;
    private String subDistrict;
    private String mainLotNumber;
    private String subLotNumber;
    private String roadNameCode;
    private String buildingMainNumber;
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
