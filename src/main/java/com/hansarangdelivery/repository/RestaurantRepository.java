package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.Restaurant;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    // 이름, 사업자 번호(Owner), 위치가 동일한 음식점이 있는지 확인하는 메서드
    boolean existsByNameAndOwnerAndLocation(String name, Long owner, UUID location);
}
