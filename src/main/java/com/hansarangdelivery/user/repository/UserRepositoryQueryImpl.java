package com.hansarangdelivery.user.repository;

import static com.hansarangdelivery.user.model.QUser.user;

import com.hansarangdelivery.user.model.User;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;


@Repository
public class UserRepositoryQueryImpl {
    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory qf;

    public UserRepositoryQueryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    public Page<User> searchUsers(Pageable pageable) {
        List<User> content = qf
            .selectFrom(user)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(pageable.getSort().stream()
                .map(order -> {
                    if (order.getProperty().equals("createdAt")) {
                        return order.isAscending() ? user.createdAt.asc() : user.createdAt.desc();
                    } else if (order.getProperty().equals("updatedAt")) {
                        return order.isAscending() ? user.updatedAt.asc() : user.updatedAt.desc();
                    }
                    return null;
                })
                .filter(Objects::nonNull) // null 값 제거
                .toArray(OrderSpecifier[]::new) // QueryDSL orderBy()에 맞게 변환
            )
            .fetch();

        long total = qf.selectFrom(user).fetch().size();

        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }
}
