package com.hansarangdelivery.review.dto;

import com.hansarangdelivery.review.model.Review;
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
public class ReviewResponseDto {

    private UUID id;

    // 리뷰 작성 시간 + 리뷰 작성자 정보로 활용
    private String createdAt;

    private String createdBy;

    private UUID orderId;

    private String content;

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
