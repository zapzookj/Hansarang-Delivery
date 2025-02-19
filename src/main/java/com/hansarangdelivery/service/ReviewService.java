package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.ReviewRequestDto;
import com.hansarangdelivery.dto.ReviewResponseDto;
import com.hansarangdelivery.entity.Review;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;


    public Double countAverageRating(UUID restaurantId) {

        return reviewRepository.getAverageRating(restaurantId);
    }

    public void addReview(ReviewRequestDto requestDto) {

        // orderId는 Unique 제약 조건으로 한 주문은 한 개의 리뷰만 작성 가능
        if (reviewRepository.existsByOrderId(requestDto.getOrderId())) {
            throw new IllegalArgumentException("이미 리뷰가 작성된 주문입니다.");
        }

        Review review = new Review(
            requestDto.getOrderId(), requestDto.getRestaurantId(), requestDto.getContent(), requestDto.getRating()
        );

        reviewRepository.save(review);
    }

    public Page<ReviewResponseDto> readRestaurantReview(UUID restaurantId, Pageable pageable) {

        Page<Review> reviews = reviewRepository.searchByRestaurantId(restaurantId, pageable);

        return reviews.map(ReviewResponseDto::new);
    }

    public Page<ReviewResponseDto> readMyReview(Long userId, Pageable pageable) {

        Page<Review> reviews = reviewRepository.searchByUserId(userId, pageable);

        return reviews.map(ReviewResponseDto::new);
    }

    public void updateReview(UUID reviewId, ReviewRequestDto requestDto, User user) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(
            () -> new IllegalArgumentException("찾는 리뷰가 없습니다.")
        );

        if (!review.getCreatedBy().equals(user.getId()) && !user.getRole().equals("ROLE_MANAGER")) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        review.update(requestDto.getContent(), requestDto.getRating());
    }

    @Transactional
    public void deleteReview(UUID reviewId, User user) {

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new IllegalArgumentException("이미 삭제되거나 리뷰 정보를 찾을 수 없습니다."));

        if (!review.getCreatedBy().equals(user.getId()) && !user.getRole().equals("ROLE_MANAGER")) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        review.delete(LocalDateTime.now(), user.getId().toString());
    }
}
