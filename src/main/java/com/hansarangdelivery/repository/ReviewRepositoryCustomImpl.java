package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.QReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Double getAverageRating(UUID restaurantId) {
        QReview review = QReview.review;

        return queryFactory
            .select(review.rating.avg().coalesce(0.0))
            .from(review)
            .where(review.restaurantId.eq(restaurantId))
            .fetchOne();
    }
}
