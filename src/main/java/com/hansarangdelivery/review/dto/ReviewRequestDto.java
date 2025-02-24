package com.hansarangdelivery.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "리뷰 작성 요청 DTO")
public class ReviewRequestDto {
    @Schema(description = "주문 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID orderId;

    @Schema(description = "레스토랑 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID restaurantId;

    @Schema(description = "리뷰 내용", example = "음식이 정말 맛있었습니다!", minLength = 5)
    @NotBlank(message = "내용은 최소 5글자 이상 입력해야합니다.")
    @Size(min = 5, message = "내용은 최소 5글자 이상 입력해야합니다.")
    private String content;

    @Schema(description = "별점", example = "5", minimum = "1", maximum = "5")
    @NotNull(message = "별점은 필수 입력값입니다.")
    @Min(value = 1, message = "별점은 최소 1점 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 최대 5점까지만 가능합니다.")
    private int rating;

    public ReviewRequestDto(String content, int rating) {
        this.content = content;
        this.rating = rating;
    }

}
