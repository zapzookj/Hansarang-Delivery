package com.hansarangdelivery.controller;

import com.hansarangdelivery.config.PageableConfig;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.dto.ReviewRequestDto;
import com.hansarangdelivery.dto.ReviewResponseDto;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResultResponseDto<Void> createReview(@RequestBody ReviewRequestDto requestDto) {

        reviewService.createReview(requestDto);

        return new ResultResponseDto<>("리뷰 작성 완료", 200);
    }

    @GetMapping("/{reviewId}")
    public ResultResponseDto<ReviewResponseDto> readReview(@PathVariable UUID reviewId) {

        ReviewResponseDto responseDto = reviewService.readReview(reviewId);

        return new ResultResponseDto<>("리뷰 조회 완료", 200, responseDto);
    }

    @GetMapping("/")
    public ResultResponseDto<Page<ReviewResponseDto>> searchRestaurantReview(@RequestParam UUID restaurantId, Pageable pageable) {

        PageableConfig.validatePageSize(pageable);

        Page<ReviewResponseDto> responseList = reviewService.searchRestaurantReview(restaurantId, pageable);

        return new ResultResponseDto<>("식당 리뷰 조회 성공", 200, responseList);
    }

    @GetMapping("/me")
    public ResultResponseDto<Page<ReviewResponseDto>> searchMyReview(@AuthenticationPrincipal UserDetailsImpl userDetails, Pageable pageable) {

        PageableConfig.validatePageSize(pageable);

        Page<ReviewResponseDto> responseList = reviewService.searchMyReview(userDetails.getUser().getId(), pageable);

        return new ResultResponseDto<>("나의 리뷰 조회 성공", 200, responseList);
    }

    @PutMapping("/{reviewId}")
    public ResultResponseDto<Void> updateReview(@PathVariable UUID reviewId,
                                                @RequestBody ReviewRequestDto requestDto,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        reviewService.updateReview(reviewId, requestDto, userDetails.getUser());

        return new ResultResponseDto<>("리뷰 수정 완료", 200);
    }

    @DeleteMapping("/{reviewId}")
    public ResultResponseDto<Void> deleteReview(@PathVariable UUID reviewId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        reviewService.deleteReview(reviewId, userDetails.getUser());

        return new ResultResponseDto<>("리뷰 삭제 완료", 200);
    }
}
