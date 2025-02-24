package com.hansarangdelivery.ai.dto;

import com.hansarangdelivery.ai.model.AiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "AI 응답 DTO")
public class AiResponseDto {
    @Schema(description = "AI 응답 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    @Schema(description = "AI에 대한 요청 내용", example = "고객 리뷰에 대한 응답을 작성해주세요.")
    private String request;

    @Schema(description = "AI가 생성한 응답", example = "고객님의 소중한 리뷰 감사합니다. 저희 서비스 개선에 큰 도움이 됩니다.")
    private String response;

    public AiResponseDto(AiResponse aiResponse) {
        this.id = aiResponse.getId();
        this.request = aiResponse.getRequest();
        this.response = aiResponse.getResponse();
    }

}
