package com.hansarangdelivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotBlank(message = "카테고리 이름은 비어 있을 수 없습니다.")
    @Size(min = 2, max = 50, message = "카테고리 이름은 2자 이상 50자 이하로 입력하세요.")
    private String name;
}
