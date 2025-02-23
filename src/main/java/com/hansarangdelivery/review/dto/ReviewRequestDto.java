package com.hansarangdelivery.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    private UUID orderId;

    private UUID restaurantId;

    private String content;

    private int rating;

    public ReviewRequestDto(String content, int rating) {
        this.content = content;
        this.rating = rating;
    }
}
