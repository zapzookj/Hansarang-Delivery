package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.RestaurantRequestDto;
import com.hansarangdelivery.dto.RestaurantResponseDto;
import com.hansarangdelivery.entity.Restaurant;
import com.hansarangdelivery.repository.CategoryRepository;
import com.hansarangdelivery.repository.LocationRepository;
import com.hansarangdelivery.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    public void register(RestaurantRequestDto requestDto) {
        String name = requestDto.getName();
        UUID categoryId = requestDto.getCategory_id();
        UUID ownerId = requestDto.getOwner_id();
        UUID locationId = requestDto.getLocation_id();

        // 입력 값 검증(가게 이름, 카테고리, 위치 ) TODO: owner에 대한 검증 어떻게 할지
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("가게 이름은 반드시 입력해야 합니다.");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("가게 이름은 255자 이내여야 합니다.");
        }
        if (categoryId == null || !categoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("유효하지 않은 카테고리입니다.");
        }

        if (locationId == null || !locationRepository.existsById(locationId)) {
            throw new IllegalArgumentException("유효하지 않은 위치입니다.");
        }

        Restaurant restaurant = new Restaurant(name, categoryId,ownerId,locationId);
        if (restaurantRepository.existsByNameAndOwnerAndLocation(restaurant.getName(), restaurant.getOwner(), restaurant.getLocation())) {
            throw new IllegalArgumentException("이미 같은 음식점이 존재합니다.");
        }

        restaurantRepository.save(restaurant);
    }

    public RestaurantResponseDto getRestaurantInfo(UUID restaurantId) {
        Restaurant restaurant = check(restaurantId);
        return new RestaurantResponseDto(restaurant);
    }

    @Transactional
    public void updateRestaurant(UUID restaurantId, RestaurantRequestDto requestDto) {
        Restaurant restaurant = check(restaurantId);

        updateIfNotNull(requestDto.getName(), restaurant::setName, restaurant.getName());
        updateIfNotNull(requestDto.getOwner_id(), restaurant::setOwner, restaurant.getOwner());
        updateIfNotNull(requestDto.getLocation_id(), restaurant::setLocation, restaurant.getLocation());

        // categoryId는 필수 값이므로 무조건 업데이트
        if (!requestDto.getCategory_id().equals(restaurant.getCategory())) {
            restaurant.setCategory(requestDto.getCategory_id());
        }
    }

    @Transactional
    public void deactivateRestaurant(UUID restaurantId) {
        Restaurant restaurant = check(restaurantId);

        // 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String deletedBy = authentication.getName(); // 인증된 사용자의 username을 deletedBy로 사용

        // 삭제 처리(삭제자와 삭제날짜)
        restaurant.delete(LocalDateTime.now(),deletedBy);

        restaurantRepository.save(restaurant); // 수정된 음식점 정보 저장
    }

    private <T> void updateIfNotNull(T newValue, Consumer<T> setter, T currentValue) {
        if (newValue != null && !Objects.equals(newValue,currentValue)) {
            setter.accept(newValue);
        }
    }

    private Restaurant check(UUID restaurantId){
//        음식점 존재 여부 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new EntityNotFoundException("해당 ID의 음식점을 찾을 수 없습니다."));

        if(restaurant.getDeletedAt()!=null){
            throw new EntityNotFoundException("해당 가게는 삭제되었습니다.");
        }

        return restaurant;
    }


}
