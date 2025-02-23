package com.hansarangdelivery.api;

import com.hansarangdelivery.config.PageableConfig;
import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.global.dto.ResultResponseDto;
import com.hansarangdelivery.review.dto.ReviewRequestDto;
import com.hansarangdelivery.review.dto.ReviewResponseDto;
import com.hansarangdelivery.review.service.ReviewService;
import com.hansarangdelivery.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
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

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResultResponseDto<ReviewResponseDto> createReview(@RequestBody ReviewRequestDto requestDto) {

        ReviewResponseDto responseDto = reviewService.createReview(requestDto);

        return new ResultResponseDto<>("리뷰 작성 완료", 200, responseDto);
    }

    @GetMapping("/{reviewId}")
    public ResultResponseDto<ReviewResponseDto> readReview(@PathVariable UUID reviewId) {

        ReviewResponseDto responseDto = reviewService.readReview(reviewId);

        return new ResultResponseDto<>("리뷰 조회 완료", 200, responseDto);
    }

    @GetMapping()
    public ResultResponseDto<PageResponseDto<ReviewResponseDto>> searchRestaurantReview(@RequestParam UUID restaurantId, Pageable pageable) {

        PageableConfig.validatePageSize(pageable);

        PageResponseDto<ReviewResponseDto> responseList = reviewService.searchRestaurantReview(restaurantId, pageable);

        return new ResultResponseDto<>("식당 리뷰 조회 성공", 200, responseList);
    }

    @GetMapping("/me")
    public ResultResponseDto<PageResponseDto<ReviewResponseDto>> searchMyReview(@AuthenticationPrincipal UserDetailsImpl userDetails, Pageable pageable) {

        PageableConfig.validatePageSize(pageable);

        PageResponseDto<ReviewResponseDto> responseList = reviewService.searchMyReview(userDetails.getUser().getId(), pageable);

        return new ResultResponseDto<>("나의 리뷰 조회 성공", 200, responseList);
    }

    @PutMapping("/{reviewId}")
    public ResultResponseDto<ReviewResponseDto> updateReview(@PathVariable UUID reviewId,
                                                             @RequestBody ReviewRequestDto requestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ReviewResponseDto responseDto = reviewService.updateReview(reviewId, requestDto, userDetails.getUser());

        return new ResultResponseDto<>("리뷰 수정 완료", 200, responseDto);
    }

    @DeleteMapping("/{reviewId}")
    public ResultResponseDto<ReviewResponseDto> deleteReview(@PathVariable UUID reviewId,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ReviewResponseDto responseDto = reviewService.deleteReview(reviewId, userDetails.getUser());

        return new ResultResponseDto<>("리뷰 삭제 완료", 200, responseDto);
    }
}
