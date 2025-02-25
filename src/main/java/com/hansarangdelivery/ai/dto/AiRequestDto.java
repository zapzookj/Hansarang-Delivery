package com.hansarangdelivery.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI 요청 DTO")
public class AiRequestDto {
    @Schema(description = "AI에 대한 요청 내용", example = "고객 리뷰에 대한 응답을 작성해주세요.", required = true)
    private String request;
}
