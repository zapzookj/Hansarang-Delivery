package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.RestaurantRequestDto;
import com.hansarangdelivery.entity.Category;
import com.hansarangdelivery.entity.Restaurant;
import com.hansarangdelivery.repository.RestaurantRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    // TODO : Owner Table에서 owner 사업자 번호 가져올건지, User에서 처리할건지
    //  확인하고 처리(Restaurant 엔티티, 테이블문서, ERD, 처리에 맞는 repository 주입)
    //  validation 추가 (존재 여부- category, location, businessNum)
    public void register(RestaurantRequestDto requestDto) {
        String name = requestDto.getName();
        UUID categoryId = requestDto.getCategory_id();
        UUID ownerId = requestDto.getOwner_id();
        UUID locationId = requestDto.getLocation_id();


        Restaurant restaurant = new Restaurant(name, categoryId,ownerId,locationId);
        restaurantRepository.save(restaurant);
    }
}
