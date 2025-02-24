package com.hansarangdelivery.api;

import com.hansarangdelivery.config.PageableConfig;
import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.global.dto.ResultResponseDto;
import com.hansarangdelivery.review.dto.ReviewRequestDto;
import com.hansarangdelivery.review.dto.ReviewResponseDto;
import com.hansarangdelivery.review.service.ReviewService;
import com.hansarangdelivery.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Review", description = "리뷰 관리 API")
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성", description = "새로운 리뷰를 작성합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 작성 완료")
    @ApiResponse(responseCode = "403", description = "권한 필요", content = @Content)
    @ApiResponse(responseCode = "400", description = "값 작성 규칙 지켜지지 않음", content = @Content)
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResultResponseDto<ReviewResponseDto> createReview(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                schema = @Schema(implementation = ReviewRequestDto.class),
                examples = {
                    @ExampleObject(
                        name = "리뷰 작성 예시",
                        value = """
                    {
                      "content": "맛있었습니다!",
                      "rating": 5,
                      "restaurantId": "123e4567-e89b-12d3-a456-426614174000"
                    }
                    """
                    )
                }
            )
        )
        @RequestBody ReviewRequestDto requestDto) {
        ReviewResponseDto responseDto = reviewService.createReview(requestDto);
        return new ResultResponseDto<>("리뷰 작성 완료", 200, responseDto);
    }

    @Operation(summary = "리뷰 조회", description = "특정 리뷰를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 조회 완료")
    @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음", content = @Content)
    @GetMapping("/{reviewId}")
    public ResultResponseDto<ReviewResponseDto> readReview(
        @Parameter(description = "리뷰 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID reviewId) {
        ReviewResponseDto responseDto = reviewService.readReview(reviewId);
        return new ResultResponseDto<>("리뷰 조회 완료", 200, responseDto);
    }

    @Operation(summary = "식당 리뷰 검색", description = "특정 식당의 모든 리뷰를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "식당 리뷰 조회 성공")
    @Parameters({
        @Parameter(name = "restaurantId", description = "식당 ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000"),
        @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
        @Parameter(name = "size", description = "페이지당 항목 수", example = "10"),
        @Parameter(name = "sort", description = "정렬 기준 (예: createdAt,desc)", example = "createdAt,desc")
    })
    @GetMapping()
    public ResultResponseDto<PageResponseDto<ReviewResponseDto>> searchRestaurantReview(
        @RequestParam UUID restaurantId,
        @Parameter(hidden = true) Pageable pageable) {
        PageableConfig.validatePageSize(pageable);
        PageResponseDto<ReviewResponseDto> responseList = reviewService.searchRestaurantReview(restaurantId, pageable);
        return new ResultResponseDto<>("식당 리뷰 조회 성공", 200, responseList);
    }

    @Operation(summary = "내 리뷰 조회", description = "로그인한 사용자의 모든 리뷰를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "나의 리뷰 조회 성공")
    @ApiResponse(responseCode = "403", description = "권한 필요", content = @Content)
    @Parameters({
        @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
        @Parameter(name = "size", description = "페이지당 항목 수", example = "10"),
        @Parameter(name = "sort", description = "정렬 기준 (예: createdAt,desc)", example = "createdAt,desc")
    })
    @GetMapping("/me")
    public ResultResponseDto<PageResponseDto<ReviewResponseDto>> searchMyReview(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Parameter(hidden = true) Pageable pageable) {
        PageableConfig.validatePageSize(pageable);
        PageResponseDto<ReviewResponseDto> responseList = reviewService.searchMyReview(userDetails.getUser().getId(), pageable);
        return new ResultResponseDto<>("나의 리뷰 조회 성공", 200, responseList);
    }

    @Operation(summary = "리뷰 수정", description = "기존 리뷰를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 수정 완료")
    @ApiResponse(responseCode = "403", description = "권한 필요", content = @Content)
    @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음", content = @Content)
    @ApiResponse(responseCode = "400", description = "값 작성 규칙 지켜지지 않음", content = @Content)
    @PutMapping("/{reviewId}")
    public ResultResponseDto<ReviewResponseDto> updateReview(
        @Parameter(description = "리뷰 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID reviewId,
        @RequestBody ReviewRequestDto requestDto,
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponseDto responseDto = reviewService.updateReview(reviewId, requestDto, userDetails.getUser());
        return new ResultResponseDto<>("리뷰 수정 완료", 200, responseDto);
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 삭제 완료")
    @ApiResponse(responseCode = "403", description = "권한 필요", content = @Content)
    @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음", content = @Content)
    @DeleteMapping("/{reviewId}")
    public ResultResponseDto<ReviewResponseDto> deleteReview(
        @Parameter(description = "리뷰 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID reviewId,
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponseDto responseDto = reviewService.deleteReview(reviewId, userDetails.getUser());
        return new ResultResponseDto<>("리뷰 삭제 완료", 200, responseDto);
    }
}
