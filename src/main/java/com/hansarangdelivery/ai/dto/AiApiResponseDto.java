package com.hansarangdelivery.ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "AI API 응답 DTO")
public class AiApiResponseDto {
    @Schema(description = "AI 응답 후보 목록")
    private List<Candidate> candidates;

    @Getter
    @Setter
    @Schema(description = "AI 응답 후보")
    public static class Candidate {
        @Schema(description = "응답 내용")
        private Content content;
    }

    @Getter
    @Setter
    @Schema(description = "응답 내용")
    public static class Content {
        @Schema(description = "응답 부분 목록")
        private List<Part> parts;
    }

    @Getter
    @Setter
    @Schema(description = "응답 부분")
    public static class Part {
        @Schema(description = "텍스트 내용", example = "AI가 생성한 응답 텍스트")
        private String text;
    }

}
