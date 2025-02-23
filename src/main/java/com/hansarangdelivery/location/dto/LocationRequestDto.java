package com.hansarangdelivery.location.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequestDto {
    // 페이징 파라미터
    private int page = 0; // 기본값
    private int size = 10; // 기본값

    // 검색 파라미터
    @NotBlank(message = "시/도(city)는 필수 입력값입니다.")
    private String city;
    @NotBlank(message = "시군구(district)는 필수 입력값입니다.")
    private String district;
    private String subDistrict;
    private String mainLotNumber;
    private String subLotNumber;
    private String buildingMainNumber;
    private String buildingSubNumber;
}
