package com.hansarangdelivery.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "카테고리 응답 DTO")
public class CategoryResponseDto {
    @Schema(description = "카테고리 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    @Schema(description = "카테고리 이름", example = "한식")
    private String name;
}
