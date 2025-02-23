package com.hansarangdelivery.review.service;

import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.review.dto.ReviewRequestDto;
import com.hansarangdelivery.review.dto.ReviewResponseDto;
import com.hansarangdelivery.review.model.Review;
import com.hansarangdelivery.order.service.OrderStatusService;
import com.hansarangdelivery.user.model.User;
import com.hansarangdelivery.user.model.UserRole;
import com.hansarangdelivery.global.exception.DuplicateResourceException;
import com.hansarangdelivery.global.exception.ForbiddenActionException;
import com.hansarangdelivery.global.exception.ResourceNotFoundException;
import com.hansarangdelivery.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderStatusService orderStatusService;

    public Double countAverageRating(UUID restaurantId) {

        return reviewRepository.getAverageRating(restaurantId);
    }

    @CacheEvict(value = "reviews", allEntries = true)
    public ReviewResponseDto createReview(ReviewRequestDto requestDto) {

        // orderId는 Unique 제약 조건으로 한 주문은 한 개의 리뷰만 작성 가능
        if (reviewRepository.existsByOrderId(requestDto.getOrderId())) {
            throw new DuplicateResourceException("이미 리뷰가 작성된 주문입니다.");
        }
        if (!orderStatusService.isCompleted(requestDto.getOrderId())) {
            throw new ForbiddenActionException("리뷰 작성 권한이 없습니다.[Error: 완료되지 않은 주문]");
        }

        Review review = new Review(
            requestDto.getOrderId(), requestDto.getRestaurantId(), requestDto.getContent(), requestDto.getRating()
        );

        reviewRepository.save(review);

        return new ReviewResponseDto(review);
    }

    public ReviewResponseDto readReview(UUID reviewId) {

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new IllegalArgumentException("찾는 리뷰가 없습니다."));

        return new ReviewResponseDto(review);
    }

    @Cacheable(
        value = "reviews",
        key = "#restaurantId + ':' + #pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort.toString()"
    )
    public PageResponseDto<ReviewResponseDto> searchRestaurantReview(UUID restaurantId, Pageable pageable) {

        Page<Review> reviews = reviewRepository.searchByRestaurantId(restaurantId, pageable);

        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("작성된 리뷰가 없습니다");
        }

        return new PageResponseDto<>(reviews.map(ReviewResponseDto::new));
    }

    @Cacheable(
        value = "reviews",
        key = "#userId + ':' + #pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort.toString()"
    )
    public Page<ReviewResponseDto> searchMyReview(Long userId, Pageable pageable) {

        String userIdStr = String.valueOf(userId);

        Page<Review> reviews = reviewRepository.searchByUserId(userIdStr, pageable);

        return reviews.map(ReviewResponseDto::new);
    }

    @CacheEvict(value = "reviews", allEntries = true)
    public ReviewResponseDto updateReview(UUID reviewId, ReviewRequestDto requestDto, User user) {

        String userId = String.valueOf(user.getId());

        Review review = reviewRepository.findById(reviewId).orElseThrow(
            () -> new IllegalArgumentException("찾는 리뷰가 없습니다.")
        );

        if (!review.getCreatedBy().equals(userId)
            && !user.getRole().equals(UserRole.MANAGER)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        review.update(requestDto.getContent(), requestDto.getRating());

        return new ReviewResponseDto(review);
    }

    @CacheEvict(value = "reviews", allEntries = true)
    @Transactional
    public ReviewResponseDto deleteReview(UUID reviewId, User user) {

        String userId = String.valueOf(user.getId());

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new IllegalArgumentException("이미 삭제되거나 리뷰 정보를 찾을 수 없습니다."));

        if (!review.getCreatedBy().equals(userId)
            && !user.getRole().equals(UserRole.MANAGER)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        review.delete(LocalDateTime.now(), user.getId().toString());

        return new ReviewResponseDto(review);
    }
}
