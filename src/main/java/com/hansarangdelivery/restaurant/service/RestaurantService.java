package com.hansarangdelivery.restaurant.service;

import com.hansarangdelivery.location.dto.LocationResponseDto;
import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.restaurant.dto.RestaurantRequestDto;
import com.hansarangdelivery.restaurant.dto.RestaurantResponseDto;
import com.hansarangdelivery.restaurant.model.Restaurant;
import com.hansarangdelivery.review.service.ReviewService;
import com.hansarangdelivery.category.service.CategoryService;
import com.hansarangdelivery.location.service.LocationService;
import com.hansarangdelivery.menu.service.MenuItemService;
import com.hansarangdelivery.user.model.User;
import com.hansarangdelivery.global.exception.DuplicateResourceException;
import com.hansarangdelivery.global.exception.ResourceNotFoundException;
import com.hansarangdelivery.restaurant.repository.RestaurantRepository;
import com.hansarangdelivery.restaurant.repository.RestaurantRepositoryImpl;

import com.hansarangdelivery.user.service.UserService;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantRepositoryImpl restaurantRepositoryQuery;

    private final CategoryService categoryService;
    private final LocationService locationService;
    private final UserService userService;
    private final MenuItemService menuItemService;
    private final ReviewService reviewService;

    public RestaurantResponseDto register(RestaurantRequestDto requestDto) {
//      가게 등록하기
        RestaurantRequestDto request = checkedRequest(requestDto);
        Restaurant restaurant = new Restaurant(request);
        double point = 0;
        return new RestaurantResponseDto(restaurantRepository.save(restaurant),point);
    }

    public RestaurantResponseDto getRestaurantInfo(UUID restaurantId) {
//      가게 정보 조회하기 - 자세하게 보여줌
        Restaurant restaurant = checkedRestaurant(restaurantId);
        return getRestaurantResponseDto(restaurant);
    }

    @Transactional
    public RestaurantResponseDto updateRestaurant(UUID restaurantId, RestaurantRequestDto requestDto) {
        Restaurant restaurant = checkedRestaurant(restaurantId);
        restaurant.update(requestDto); // 찾은 음식점의 정보를 요청을 토대로 수정
        restaurantRepository.save(restaurant);
        return getRestaurantResponseDto(restaurant);
    }

    private RestaurantResponseDto getRestaurantResponseDto(Restaurant restaurant) {
        LocationResponseDto location = locationService.readLocation(restaurant.getLocation());
        String location_str = location.getCity() + " " + location.getDistrict() + " " + location.getSubDistrict();
        String category = categoryService.getCategoryById(restaurant.getCategory()).getName();
        double point = reviewService.countAverageRating(restaurant.getId());
        return new RestaurantResponseDto(restaurant.getId(), restaurant.getName(), location_str, restaurant.getStatus(), category,point);
    }

    @Transactional
    public RestaurantResponseDto deleteRestaurant(User user, UUID restaurantId) {
        Restaurant restaurant = checkedRestaurant(restaurantId);
        String deletedBy = user.getId().toString(); // 인증된 사용자의 username을 deletedBy로 사용

        restaurant.delete(LocalDateTime.now(),deletedBy);// 삭제 처리(삭제자와 삭제날짜)
        if(menuItemService.isExist(restaurantId)){
            menuItemService.deleteMenuItemByRestaurantId(restaurantId,user); // 메뉴도 같이
        }
        double point = reviewService.countAverageRating(restaurant.getId());
        return new RestaurantResponseDto(restaurantRepository.save(restaurant),point);
    }

    public PageResponseDto<RestaurantResponseDto> searchRestaurants(Pageable pageable, String search, String category) {
        //  검색 조건에 맞는 음식점 리스트를 정렬해서 전달
        UUID categoryId=null;
        if(category!=null){
            categoryId=categoryService.getByName(category).getId();
        }
        Page<RestaurantResponseDto> mappedPage = restaurantRepositoryQuery.
            searchRestaurant(pageable, search,categoryId)
            .map((restaurant)->{
                double point = reviewService.countAverageRating(restaurant.getId());
                return new RestaurantResponseDto(restaurant,point);
                }

            );

        return new PageResponseDto<>(mappedPage);
    }

    private Restaurant checkedRestaurant(UUID restaurantId) {
//        음식점 존재 여부 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ResourceNotFoundException("유효하지 않은 음식점입니다."));
        if (restaurant.getDeletedAt() != null) {
            throw new ResourceNotFoundException("유효하지 않은 음식점입니다.");
        }
        return restaurant;
    }


    public Restaurant getRestaurantById(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
            .filter(restaurant -> restaurant.getDeletedAt() == null) // 삭제된 가게인지 확인
            .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 음식점을 찾을 수 없습니다: " + restaurantId));
    }

    private RestaurantRequestDto checkedRequest(RestaurantRequestDto requestDto) {
        String name = requestDto.getName();
        UUID categoryId = requestDto.getCategory_id();
        Long ownerId = requestDto.getOwner_id();
        UUID locationId = requestDto.getLocation_id();

        if (categoryId == null || !categoryService.existsById(categoryId)) {
            throw new ResourceNotFoundException("유효하지 않은 카테고리입니다.");
        }
        if (locationId == null || !locationService.existsById(locationId)) {
            throw new ResourceNotFoundException("유효하지 않은 위치입니다.");
        }
        if (ownerId == null || !userService.isOwner(ownerId)) {
            throw new ResourceNotFoundException("유효하지 않은 가게주인입니다.");
        }
        if (restaurantRepository.existsByNameAndOwnerAndLocation(name, ownerId, locationId)) {
            throw new DuplicateResourceException("이미 같은 음식점이 존재합니다.");
        }
        return requestDto;
    }
}
