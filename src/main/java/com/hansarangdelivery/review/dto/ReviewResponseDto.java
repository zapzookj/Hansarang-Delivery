package com.hansarangdelivery.review.dto;

import com.hansarangdelivery.review.model.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "리뷰 응답 DTO")
public class ReviewResponseDto {
    @Schema(description = "리뷰 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "리뷰 작성 시간", example = "2023-06-15T14:30:00")
    private String createdAt;

    @Schema(description = "리뷰 작성자", example = "user123")
    private String createdBy;

    @Schema(description = "주문 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID orderId;

    @Schema(description = "리뷰 내용", example = "음식이 정말 맛있었습니다!")
    private String content;

    @Schema(description = "별점", example = "5", minimum = "1", maximum = "5")
    private int rating;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.createdAt = review.getCreatedAt().toString();
        this.createdBy = review.getCreatedBy();
        this.orderId = review.getOrderId();
        this.content = review.getContent();
        this.rating = review.getRating();
    }

}
