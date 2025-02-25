package com.hansarangdelivery.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "카테고리 요청 DTO")
public class CategoryRequestDto {
    @Schema(description = "카테고리 이름", example = "한식", required = true, minLength = 2, maxLength = 50)
    @NotBlank(message = "카테고리 이름은 비어 있을 수 없습니다.")
    @Size(min = 2, max = 50, message = "카테고리 이름은 2자 이상 50자 이하로 입력하세요.")
    private String name;
}
