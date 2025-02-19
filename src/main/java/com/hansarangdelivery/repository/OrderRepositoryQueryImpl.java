package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.Order;
import com.hansarangdelivery.entity.QOrder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryQueryImpl implements OrderRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> searchOrders(Pageable pageable, String search) {
        QOrder order = QOrder.order;
        BooleanBuilder builder = new BooleanBuilder();

       // 가게이름, 메뉴 이름
        if (search != null && !search.isBlank()) {
            builder.and(order.storeName.containsIgnoreCase(search)
                .or(order.orderItems.any().menuName.containsIgnoreCase(search)));

        }

        List<Order> results = queryFactory
            .selectFrom(order)
            .where(builder)
            .orderBy(order.createdAt.desc(), order.updatedAt.desc()) // 생성일 최신순 → 수정일 최신순
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        //전체 개수 조회
        long total = queryFactory
            .select(order.count())
            .from(order)
            .where(builder)
            .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }



    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        QOrder order = QOrder.order;

        List<Order> results = queryFactory
            .selectFrom(order)
            .orderBy(order.createdAt.desc(), order.updatedAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = queryFactory
            .select(order.count())
            .from(order)
            .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }


}
