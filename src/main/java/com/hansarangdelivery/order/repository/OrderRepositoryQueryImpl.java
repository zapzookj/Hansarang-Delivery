package com.hansarangdelivery.order.repository;

import com.hansarangdelivery.order.model.Order;
import com.hansarangdelivery.order.model.QOrder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryQueryImpl implements OrderRepositoryQuery {
    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Order> searchByOrderId(UUID orderId, Pageable pageable) {

        Sort sort = pageable.getSort();

        QOrder order = QOrder.order;

        JPAQuery<Order> query = queryFactory
            .selectFrom(order)
            .where(order.id.eq(orderId)) // 주문 ID로 필터링
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        // 정렬 처리
        if (sort.iterator().hasNext() && sort.iterator().next().isAscending()) {
            query.orderBy(order.createdAt.asc(), order.updatedAt.asc());
        } else {
            query.orderBy(order.createdAt.desc(), order.updatedAt.desc());
        }

        List<Order> orderList = query.fetch();

        // 전체 개수 계산
        long total = queryFactory
            .selectFrom(order)
            .where(order.id.eq(orderId))
            .fetch().size();

        return new PageImpl<>(orderList, pageable, total);
    }



}
