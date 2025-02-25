package com.hansarangdelivery.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "위치 검색 요청 DTO")
public class LocationRequestDto {
    // 페이징 파라미터
    @Schema(description = "페이지 번호", example = "0", defaultValue = "0")
    private int page = 0; // 기본값
    @Schema(description = "페이지 크기", example = "10", defaultValue = "10")
    private int size = 10; // 기본값

    // 검색 파라미터
    @Schema(description = "시/도", example = "서울특별시", required = true)
    @NotBlank(message = "시/도(city)는 필수 입력값입니다.")
    private String city;

    @Schema(description = "시군구", example = "강남구", required = true)
    @NotBlank(message = "시군구(district)는 필수 입력값입니다.")
    private String district;

    @Schema(description = "읍면동", example = "역삼동")
    private String subDistrict;

    @Schema(description = "주 지번", example = "123")
    private String mainLotNumber;

    @Schema(description = "부 지번", example = "45")
    private String subLotNumber;

    @Schema(description = "건물 본번", example = "67")
    private String buildingMainNumber;

    @Schema(description = "건물 부번", example = "89")
    private String buildingSubNumber;

}
