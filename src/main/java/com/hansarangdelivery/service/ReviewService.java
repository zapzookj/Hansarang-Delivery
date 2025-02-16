package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.dto.ReviewRequestDto;
import com.hansarangdelivery.entity.Review;
import com.hansarangdelivery.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ResultResponseDto<Void> addReview(ReviewRequestDto requestDto) {
        if (reviewRepository.existsByOrderId(requestDto.getOrderId())) {
            throw new IllegalArgumentException("이미 리뷰가 작성된 주문입니다.");
        }

        Review review = new Review(
            requestDto.getOrderId(), requestDto.getContent(), requestDto.getRating()
        );

        reviewRepository.save(review);

        return new ResultResponseDto<>("리뷰 작성 완료", 200);
    }
}
