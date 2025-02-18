package com.hansarangdelivery.repository;

import static com.hansarangdelivery.entity.QRestaurant.restaurant;
import static com.hansarangdelivery.entity.QUser.user;

import com.hansarangdelivery.entity.Restaurant;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantRepositoryImpl {
    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory qf;

    public RestaurantRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    public Page<Restaurant> searchRestaurant(Pageable pageable,String search) {
        BooleanBuilder whereClause = new BooleanBuilder();

        // 검색 조건 추가 (name 컬럼에 포함되는 경우 검색)
        if (search != null && !search.isEmpty()) {
            whereClause.and(restaurant.name.containsIgnoreCase(search));
        }

        List<Restaurant> content = qf
            .selectFrom(restaurant)
            .where(whereClause)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(pageable.getSort().stream()
                .map(order -> {
                        // 정렬 기준이 여러 개 있을 때, 각 기준을 처리
                        if (order.getProperty().equals("createdAt")) {
                            return order.isAscending() ? restaurant.createdAt.asc() : restaurant.createdAt.desc();
                        } else if (order.getProperty().equals("updatedAt")) {
                            return order.isAscending() ? restaurant.updatedAt.asc() : restaurant.updatedAt.desc();
                        } else {
                            // 추가적인 정렬 기준이 있을 경우 처리
                            return null; // 필요에 따라 다른 정렬 기준을 추가
                        }
                    }
            )
            .filter(Objects::nonNull) // null 값이 나오지 않도록 필터링
            .toArray(OrderSpecifier[]::new)) // 다중 정렬 지원
            .fetch();

        long total = qf.selectFrom(restaurant).where(whereClause).fetchCount(); // 검색 조건 포함된 total 조회

        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }
}
