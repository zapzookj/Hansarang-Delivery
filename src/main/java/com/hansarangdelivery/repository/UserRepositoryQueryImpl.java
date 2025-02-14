package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hansarangdelivery.entity.QUser.user;

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
                .map(order -> order.isAscending() ? user.createdAt.asc() : user.createdAt.desc())
                .findFirst()
                .orElse(user.createdAt.desc()))
            .fetch();

        long total = qf.selectFrom(user).fetch().size();

        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }
}
