package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.dto.ReviewRequestDto;
import com.hansarangdelivery.dto.ReviewResponseDto;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/")
    public ResultResponseDto<Void> addReview(@RequestBody ReviewRequestDto requestDto) {

        reviewService.addReview(requestDto);

        return new ResultResponseDto<>("리뷰 작성 완료", 200);
    }

    @GetMapping("/{restaurantId}")
    public ResultResponseDto<Page<ReviewResponseDto>> readRestaurantReview(@PathVariable UUID restaurantId, Pageable pageable) {

        Page<ReviewResponseDto> responseList = reviewService.readRestaurantReview(restaurantId, pageable);

        return new ResultResponseDto<>("식당 리뷰 조회 성공", 200, responseList);
    }

    @GetMapping("/me/{userId}")
    public ResultResponseDto<Page<ReviewResponseDto>> readMyReview(@PathVariable UUID userId, Pageable pageable) {

        Page<ReviewResponseDto> responseList = reviewService.readMyReview(userId, pageable);

        return new ResultResponseDto<>("나의 리뷰 조회 성공", 200, responseList);
    }

    @PutMapping("/{reviewId}")
    public ResultResponseDto<Void> updateReview(@PathVariable UUID reviewId,
                                                @RequestBody ReviewRequestDto requestDto) {

        reviewService.updateReview(reviewId, requestDto);

        return new ResultResponseDto<>("리뷰 수정 완료", 200);
    }

    @DeleteMapping("/{reviewId}")
    public ResultResponseDto<Void> deleteReview(@PathVariable UUID reviewId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        reviewService.deleteReview(reviewId, userDetails.getUser());

        return new ResultResponseDto<>("리뷰 삭제 완료", 200);
    }
}
