package com.hansarangdelivery.address.repository;

import static com.hansarangdelivery.address.model.QDeliveryAddress.deliveryAddress;

import com.hansarangdelivery.address.model.DeliveryAddress;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class DeliveryAddressRepositoryQueryImpl implements DeliveryAddressRepositoryQuery {
    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory qf;

    public DeliveryAddressRepositoryQueryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }

    public void resetDefault(Long userId) {
        qf.update(deliveryAddress)
            .set(deliveryAddress.isDefault, false)
            .where(deliveryAddress.user.id.eq(userId)
                .and(deliveryAddress.isDefault.isTrue()))
            .execute();
    }

    public int countByUserId(Long userId) {
        return Math.toIntExact(
            qf.select(deliveryAddress.count())
                .from(deliveryAddress)
                .where(deliveryAddress.user.id.eq(userId))
                .fetchOne()
        );
    }

    public Optional<DeliveryAddress> findDefaultByUserId(Long userId) {
        return Optional.ofNullable(
            qf.selectFrom(deliveryAddress)
                .where(deliveryAddress.user.id.eq(userId)
                    .and(deliveryAddress.isDefault.isTrue()))
                .fetchOne()
        );
    }

    public List<DeliveryAddress> findAllByUserId(Long userId) {
        return qf.selectFrom(deliveryAddress)
            .where(deliveryAddress.user.id.eq(userId))
            .fetch();
    }

    public Optional<DeliveryAddress> findByIdAndUserId(UUID addressId, Long userId) {
        return Optional.ofNullable(
            qf.selectFrom(deliveryAddress)
                .where(deliveryAddress.id.eq(addressId)
                    .and(deliveryAddress.user.id.eq(userId)))
                .fetchOne()
        );
    }

    public boolean existsByIdAndUserId(UUID addressId, Long userId) {
        return qf.selectOne()
            .from(deliveryAddress)
            .where(deliveryAddress.id.eq(addressId)
                .and(deliveryAddress.user.id.eq(userId)))
            .fetchFirst() != null;
    }
}
