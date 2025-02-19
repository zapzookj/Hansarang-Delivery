package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.MenuItemRequestDto;
import com.hansarangdelivery.dto.MenuItemResponseDto;
import com.hansarangdelivery.entity.MenuItem;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    public void addMenuItem(MenuItemRequestDto requestDto) {

        MenuItem menuItem = new MenuItem(
            requestDto.getName(), requestDto.getPrice(), requestDto.getRestaurantId()
        );

        menuItemRepository.save(menuItem);
    }

    public List<MenuItemResponseDto> searchAllMenuItem(UUID restaurantId) {
        List<MenuItem> menuItemList = menuItemRepository.findAllByRestaurantId(restaurantId);

        if(menuItemList.isEmpty()){
            throw new IllegalArgumentException("조회된 정보가 없습니다.");
        }

        List<MenuItemResponseDto> responseList = new ArrayList<>();

        for (MenuItem menuItem : menuItemList) {
            responseList.add(new MenuItemResponseDto(menuItem.getId(), menuItem.getName(), menuItem.getPrice(), menuItem.isAvailable()));
        }

        return responseList;
    }

    public MenuItemResponseDto searchMenuItem(UUID menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
            .orElseThrow(() -> new IllegalArgumentException("찾는 메뉴가 없습니다."));

        return new MenuItemResponseDto(
            menuItem.getId(), menuItem.getName(), menuItem.getPrice(), menuItem.isAvailable()
        );
    }

    public void updateMenuItem(UUID menuItemId, MenuItemRequestDto requestDto) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
            .orElseThrow(() -> new IllegalArgumentException("찾는 메뉴가 없습니다."));

        menuItem.update(requestDto);
    }

    public void deleteMenuItem(UUID menuItemId, User user) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
            .orElseThrow(() -> new IllegalArgumentException("이미 삭제된 메뉴입니다."));

        // 삭제된 시간과 userId 를 String 으로 변환하여 저장
        menuItem.delete(LocalDateTime.now(), user.getId().toString());
    }

    // 레스토랑이 삭제될 경우 호출
    public void deleteMenuItemByRestaurantId(UUID restaurantId, User user) {
        List<MenuItem> menuItemList = menuItemRepository.findAllByRestaurantId(restaurantId);

        if(menuItemList.isEmpty()){
            throw new IllegalArgumentException("이미 삭제된 정보입니다.");
        }

        for (MenuItem menuItem : menuItemList) {
            menuItem.delete(LocalDateTime.now(), user.getId().toString());
        }
    }

    public MenuItem getMenuById(UUID menuId) {
        return menuItemRepository.findById(menuId)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 메뉴를 찾을 수 없습니다: " + menuId));
    }

}
