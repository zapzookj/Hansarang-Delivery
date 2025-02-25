package com.hansarangdelivery.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "도로명 응답 DTO")
public class RoadNameResponseDto {
    @Schema(description = "도로명 코드", example = "12345678")
    private String roadNameCode;
}
