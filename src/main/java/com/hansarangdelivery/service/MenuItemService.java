package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.MenuItemRequestDto;
import com.hansarangdelivery.dto.MenuItemResponseDto;
import com.hansarangdelivery.entity.MenuItem;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.exception.DuplicateResourceException;
import com.hansarangdelivery.exception.ResourceNotFoundException;
import com.hansarangdelivery.repository.MenuItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    public MenuItemResponseDto createMenuItem(MenuItemRequestDto requestDto) {
        MenuItem menuItem = new MenuItem(
            requestDto.getName(), requestDto.getPrice(), requestDto.getRestaurantId()
        );

        // RestaurantId 와 Name 을 기준으로 동일한 정보가 있으면 중복으로 판단
        if (isDuplicate(menuItem)) {
            throw new DuplicateResourceException("이미 존재하는 메뉴입니다.");
        }

        menuItemRepository.save(menuItem);
        return new MenuItemResponseDto(menuItem);
    }

    public Page<MenuItemResponseDto> searchAllMenuItem(UUID restaurantId, Pageable pageable) {

        Page<MenuItem> menuItems = menuItemRepository.searchMenuItemByRestaurantId(restaurantId, pageable);

        if (menuItems.isEmpty()) {
            throw new ResourceNotFoundException("조회된 메뉴가 없습니다.");
        }

        return menuItems.map(MenuItemResponseDto::new);
    }

    public MenuItemResponseDto readMenuItem(UUID menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
            .orElseThrow(() -> new ResourceNotFoundException("찾는 메뉴가 없습니다."));

        return new MenuItemResponseDto(menuItem);
    }

    @Transactional
    public MenuItemResponseDto updateMenuItem(UUID menuItemId, MenuItemRequestDto requestDto) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
            .orElseThrow(() -> new ResourceNotFoundException("찾는 메뉴가 없습니다."));

        if (isDuplicate(menuItem)) {
            throw new DuplicateResourceException("이미 존재하는 메뉴입니다.");
        }

        menuItem.update(requestDto);
        return new MenuItemResponseDto(menuItem);
    }

    @Transactional
    public MenuItemResponseDto deleteMenuItem(UUID menuItemId, User user) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
            .orElseThrow(() -> new ResourceNotFoundException("이미 삭제된 메뉴입니다."));

        // 삭제된 시간과 userId 를 String 으로 변환하여 저장
        menuItem.delete(LocalDateTime.now(), user.getId().toString());

        return new MenuItemResponseDto(menuItem);
    }

    @Transactional
    // 레스토랑이 삭제될 경우 호출
    public List<MenuItemResponseDto> deleteMenuItemByRestaurantId(UUID restaurantId, User user) {
        List<MenuItem> menuItemList = menuItemRepository.findAllByRestaurantId(restaurantId);
        List<MenuItemResponseDto> responseDtoList = new ArrayList<>();

        if (menuItemList.isEmpty()) {
            throw new ResourceNotFoundException("이미 삭제된 정보입니다.");
        }

        for (MenuItem menuItem : menuItemList) {
            menuItem.delete(LocalDateTime.now(), user.getId().toString());
            responseDtoList.add(new MenuItemResponseDto(menuItem));
        }

        return responseDtoList;
    }

    public MenuItem getMenuById(UUID menuId) {
        return menuItemRepository.findById(menuId)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 메뉴를 찾을 수 없습니다: " + menuId));
    }

    public boolean isDuplicate(MenuItem menuItem) {

        return menuItemRepository.existsByRestaurantIdAndName(menuItem.getRestaurantId(), menuItem.getName());
    }
}
