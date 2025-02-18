package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.RestaurantRequestDto;
import com.hansarangdelivery.dto.RestaurantResponseDto;
import com.hansarangdelivery.entity.Restaurant;
import com.hansarangdelivery.repository.CategoryRepository;
import com.hansarangdelivery.repository.LocationRepository;
import com.hansarangdelivery.repository.RestaurantRepository;
import com.hansarangdelivery.repository.RestaurantRepositoryImpl;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantRepositoryImpl restaurantRepositoryQuery;

    private final CategoryService categoryService;
    private final LocationService locationService;


    public void register(RestaurantRequestDto requestDto) {
        String name = requestDto.getName();
        UUID categoryId = requestDto.getCategory_id();
        UUID ownerId = requestDto.getOwner_id();
        UUID locationId = requestDto.getLocation_id();

        // 입력 값 검증(가게 이름, 카테고리, 위치 ) TODO: owner에 대한 검증 어떻게 할지
        if ((name.equals(StringUtils.hasText(name))) || name.trim().isEmpty()) {
            throw new IllegalArgumentException("가게 이름은 반드시 입력해야 합니다.");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("가게 이름은 255자 이내여야 합니다.");
        }
        if (categoryId == null || !categoryService.existsById(categoryId)) {
            throw new IllegalArgumentException("유효하지 않은 카테고리입니다.");
        }

        if (locationId == null || !locationService.existsById(locationId)) {
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

        restaurant.update(requestDto);
        if(requestDto.isOpen()){
            restaurant.open();
        }else{
            restaurant.close();
        }
        restaurantRepository.save(restaurant); // 수정된 음식점 정보 저장
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

    public Page<RestaurantResponseDto> searchRestaurants(int page, int size, String sort, String direction , String search) {
        //  음식점을 찾는 메서드

        page = page-1;

        // 파라미터 유효성 검사(size, sort, direction)
        if (size != 10 && size != 30 && size != 50) {
            size = 10; // 허용되지 않은 size는 기본값 10으로 설정
        }

        // sort 값이 여러 개 들어올 수 있도록 처리
        List<String> validSortFields = List.of("createdAt", "updatedAt");
        List<Order> orders = new ArrayList<>();

        for (String sortField : sort.split(",")) { // "createdAt", "updatedAt" 정렬 기준을 지원
            if (validSortFields.contains(sortField)) {
                orders.add(new Order(direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));
            }
        }

        if (orders.isEmpty()) {
            orders.add(new Order(Sort.Direction.DESC, "createdAt"));  // 기본: 생성일 최신순
            orders.add(new Order(Sort.Direction.DESC, "updatedAt"));  // 생성일 같을때 : 수정일 최신순
        }

        // Pageable 생성
        Sort sortBy = Sort.by(orders);
        Pageable pageable = PageRequest.of(page, size, sortBy);

        return restaurantRepositoryQuery.searchRestaurant(pageable,search).map(RestaurantResponseDto::new);
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

    public Restaurant getRestaurantById(UUID storeId) {
        return restaurantRepository.findById(storeId)
            .filter(restaurant -> restaurant.getDeletedAt() == null) // 삭제된 가게인지 확인
            .orElseThrow(() -> new EntityNotFoundException("해당 ID의 음식점을 찾을 수 없습니다: " + storeId));
    }

}
