package com.hansarangdelivery.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReviewRequestDto {

    private UUID orderId;

    private UUID restaurantId;

    private String content;

    private int rating;
}
