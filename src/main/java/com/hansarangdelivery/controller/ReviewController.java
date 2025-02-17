package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.dto.ReviewRequestDto;
import com.hansarangdelivery.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResultResponseDto<Void> addReview(@RequestBody ReviewRequestDto requestDto) {
        return reviewService.addReview(requestDto);
    }
}
