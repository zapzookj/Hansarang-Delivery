//package com.hansarangdelivery.controller;
//
//import com.hansarangdelivery.dto.ResultResponseDto;
//import com.hansarangdelivery.dto.ReviewRequestDto;
//import com.hansarangdelivery.dto.ReviewResponseDto;
//import com.hansarangdelivery.service.ReviewService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/reviews")
//public class ReviewController {
//
//    private final ReviewService reviewService;
//
//    @PostMapping("/")
//    public ResultResponseDto<Void> addReview(@RequestBody ReviewRequestDto requestDto) {
//        return reviewService.addReview(requestDto);
//    }
//
//    @GetMapping("/{restaurantId}")
//    public ResultResponseDto<List<ReviewResponseDto>> readReview(@PathVariable UUID restaurantId) {
//        return reviewService.readReview(restaurantId);
//    }
//
//    @GetMapping("/me/{userId}")
//    public ResultResponseDto<List<ReviewResponseDto>> readMyReview(@PathVariable UUID userId) {
//        return reviewService.readMyReview(userId);
//    }
//
//    @PutMapping("/{reviewId}")
//    public ResultResponseDto<Void> updateReview(@PathVariable UUID reviewId,
//                                                @RequestBody ReviewRequestDto requestDto) {
//        return reviewService.updateReview(reviewId, requestDto);
//    }
//
//    @DeleteMapping("/{reviewId}")
//    public ResultResponseDto<Void> deleteReview(@PathVariable UUID reviewId) {
//        return reviewService.deleteReview(reviewId);
//    }
//}
