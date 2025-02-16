package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.MenuItemRequestDto;
import com.hansarangdelivery.dto.MenuItemResponseDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.entity.MenuItem;
import com.hansarangdelivery.entity.Restaurant;
import com.hansarangdelivery.repository.MenuItemRepository;
import com.hansarangdelivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public ResponseEntity<ResultResponseDto> addMenuItem(MenuItemRequestDto requestDto) {
        Restaurant restaurant = restaurantRepository.findById(requestDto.getRestaurantId()).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));;

        MenuItem menuItem = new MenuItem(
            requestDto.getName(), requestDto.getPrice(), restaurant
        );

        menuItemRepository.save(menuItem);
        return ResponseEntity.ok().body(new ResultResponseDto("메뉴가 추가되었습니다.", 200));
    }

    /*
        RestaurantService 로 이동할 예정 (RESTful 최대한 지키기 위해)
        public ResponseEntity<List<MenuItemResponseDto>> searchMenuItemsByRestaurant(UUID restaurantId) {
            Restaurant restaurant = restaurantRepository.findById(restaurantId);

            if (restaurant == null) {
                throw new IllegalArgumentException("등록된 식당이 없습니다.");
            }

            List<MenuItem> menuItemList = menuItemRepository.findAllByRestaurant(restaurant)
                .orElseThrow(() -> new IllegalArgumentException("등록된 메뉴가 없습니다."));

            List<MenuItemResponseDto> responseDtoList = new ArrayList<>();

            for (MenuItem menuItem : menuItemList) {
                responseDtoList.add(
                    new MenuItemResponseDto(menuItem.getId(), menuItem.getName(), menuItem.getPrice(), menuItem.isAvailable())
                );
            }

            return ResponseEntity.ok(responseDtoList);
        }
    */
    public ResponseEntity<MenuItemResponseDto> searchMenuItem(UUID menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
            .orElseThrow(() -> new IllegalArgumentException("찾는 메뉴가 없습니다."));

        return ResponseEntity.ok(
            new MenuItemResponseDto(menuItem.getId(), menuItem.getName(), menuItem.getPrice(), menuItem.isAvailable())
        );
    }

    public ResponseEntity<ResultResponseDto> updateMenuItem(UUID menuItemId, MenuItemRequestDto requestDto) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
            .orElseThrow(() -> new IllegalArgumentException("찾는 메뉴가 없습니다."));

        menuItem.update(requestDto);

        return ResponseEntity.ok(
            new ResultResponseDto("수정되었습니다.", 201)
        );
    }
}
