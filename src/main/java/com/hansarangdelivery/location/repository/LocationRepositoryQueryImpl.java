package com.hansarangdelivery.location.repository;

import static com.hansarangdelivery.location.model.QLocation.location;

import com.hansarangdelivery.location.dto.LocationRequestDto;
import com.hansarangdelivery.location.model.Location;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;


@Repository
public class LocationRepositoryQueryImpl implements LocationRepositoryQuery {

    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory qf;

    public LocationRepositoryQueryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }


    @Override
    public Page<Location> searchLocations(Pageable pageable, LocationRequestDto requestDto) {
        BooleanBuilder builder = setBuilder(requestDto);

        List<Location> content = qf
            .selectFrom(location)
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = qf.selectFrom(location).where(builder).fetch().size();

        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }

    private BooleanBuilder setBuilder(LocationRequestDto requestDto) {
        BooleanBuilder builder = new BooleanBuilder(); // 동적 쿼리를 위한 조건 빌더

        // 검색 조건 적용 (city와 district는 필수 입력 데이터임)
        builder.and(location.city.eq(requestDto.getCity()));
        builder.and(location.district.eq(requestDto.getDistrict()));

        // 검색 조건 적용 (해당하는 필드가 입력되었을 경우)
        if (StringUtils.hasText(requestDto.getSubDistrict())) {
            builder.and(location.subDistrict.eq(requestDto.getSubDistrict()));
        }
        if (StringUtils.hasText(requestDto.getMainLotNumber())) {
            builder.and(location.mainLotNumber.eq(requestDto.getMainLotNumber()));
        }
        if (StringUtils.hasText(requestDto.getSubLotNumber())) {
            builder.and(location.subLotNumber.eq(requestDto.getSubLotNumber()));
        }
        if (StringUtils.hasText(requestDto.getBuildingMainNumber())) {
            builder.and(location.buildingMainNumber.eq(requestDto.getBuildingMainNumber()));
        }
        if (StringUtils.hasText(requestDto.getBuildingSubNumber())) {
            builder.and(location.buildingSubNumber.eq(requestDto.getBuildingSubNumber()));
        }

        return builder;
    }
}
