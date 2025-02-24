package com.hansarangdelivery.review.dto;

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
public class ReviewRequestDto {

    private UUID orderId;

    private UUID restaurantId;

    @NotBlank(message = "내용은 최소 5글자 이상 입력해야합니다.")
    @Size(min = 5, message = "내용은 최소 5글자 이상 입력해야합니다.")
    private String content;

    @NotNull(message = "별점은 필수 입력값입니다.")
    @Min(value = 1, message = "별점은 최소 1점 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 최대 5점까지만 가능합니다.")
    private int rating;

    public ReviewRequestDto(String content, int rating) {
        this.content = content;
        this.rating = rating;
    }
}
