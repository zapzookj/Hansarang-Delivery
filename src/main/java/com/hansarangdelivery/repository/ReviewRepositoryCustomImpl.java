package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.QReview;
import com.hansarangdelivery.entity.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.ListType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
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

    @Override
    public Page<Review> searchByRestaurantId(UUID restaurantId, Pageable pageable) {

        QReview review = QReview.review;

        List<Review> reviewList = queryFactory
            .selectFrom(review)
            .where(review.restaurantId.eq(restaurantId))
            .orderBy(review.createdAt.desc(), review.updatedAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = queryFactory
            .selectFrom(review)
            .where(review.restaurantId.eq(restaurantId))
            .fetch().size();

        return new PageImpl<>(reviewList, pageable, total);
    }

    @Override
    public Page<Review> searchByUserId(Long userId, Pageable pageable) {

        QReview review = QReview.review;

        List<Review> reviewList = queryFactory
            .selectFrom(review)
            .where(review.createdBy.eq(userId))
            .orderBy(review.createdAt.desc(), review.updatedAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch;

        long total = queryFactory
            .selectFrom(review)
            .where(review.createdBy.eq(userId))
            .fetch().size();

        return new PageImpl<>(reviewList, pageable, total);
    }


}
