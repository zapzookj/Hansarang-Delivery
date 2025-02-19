package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.MenuItem;
import com.hansarangdelivery.entity.QMenuItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class MenuItemRepositoryQueryImpl implements MenuItemRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MenuItem> searchMenuItemByRestaurantId(UUID restaurantId, Pageable pageable) {

        QMenuItem menuItem = QMenuItem.menuItem;

        List<MenuItem> menuItemList = queryFactory
            .selectFrom(menuItem)
            .where(menuItem.restaurantId.eq(restaurantId))
            .orderBy(menuItem.createdAt.desc(), menuItem.updatedAt.desc())
            .fetch();

        long total = queryFactory
            .selectFrom(menuItem)
            .where(menuItem.restaurantId.eq(restaurantId))
            .fetch().size();

        return new PageImpl<>(menuItemList, pageable, total);
    }
}
