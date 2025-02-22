package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.QReview;
import com.hansarangdelivery.entity.Review;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

        Sort sort = pageable.getSort();

        QReview review = QReview.review;

        JPAQuery<Review> query = queryFactory
            .selectFrom(review)
            .where(review.restaurantId.eq(restaurantId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        if (sort.iterator().next().isAscending()) {
            query.orderBy(review.createdAt.asc(), review.updatedAt.asc());
        } else {
            query.orderBy(review.createdAt.desc(), review.updatedAt.desc());
        }

        List<Review> reviewList = query.fetch();

        long total = queryFactory
            .selectFrom(review)
            .where(review.restaurantId.eq(restaurantId))
            .fetch().size();

        return new PageImpl<>(reviewList, pageable, total);
    }

    @Override
    public Page<Review> searchByUserId(String userId, Pageable pageable) {

        QReview review = QReview.review;

        Sort sort = pageable.getSort();

        JPAQuery<Review> query = queryFactory
            .selectFrom(review)
            .where(review.createdBy.eq(userId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        if (sort.iterator().next().isAscending()) {
            query.orderBy(review.createdAt.asc(), review.updatedAt.asc());
        } else {
            query.orderBy(review.createdAt.desc(), review.updatedAt.desc());
        }

        List<Review> reviewList = query.fetch();

        long total = queryFactory
            .selectFrom(review)
            .where(review.createdBy.eq(userId))
            .fetch().size();

        return new PageImpl<>(reviewList, pageable, total);
    }
}
