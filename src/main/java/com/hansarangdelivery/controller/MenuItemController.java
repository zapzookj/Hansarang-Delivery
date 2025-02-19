package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.MenuItemRequestDto;
import com.hansarangdelivery.dto.MenuItemResponseDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuItemController {

    private final MenuItemService menuItemService;

    // 메뉴 추가
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<Void> addMenuItem(@RequestBody MenuItemRequestDto requestDto) {

        menuItemService.addMenuItem(requestDto);

        return new ResultResponseDto<>("메뉴 저장 성공", 200);
    }

    @GetMapping("/restaurant")
    public ResultResponseDto<Page<MenuItemResponseDto>> searchAllMenuItem(@RequestParam UUID restaurantId, Pageable pageable) {

        // PageSize 검증
        // PageableConfig.validatePageSize(pageable);

        Page<MenuItemResponseDto> responseDtoList = menuItemService.searchAllMenuItem(restaurantId, pageable);

        return new ResultResponseDto<>("메뉴 조회 성공", 200, responseDtoList);
    }

    // 메뉴 ID로 특정 메뉴 조회
    @GetMapping("/{menuItemId}")
    public ResultResponseDto<MenuItemResponseDto> readMenuItem(@PathVariable UUID menuItemId) {

        MenuItemResponseDto responseDto = menuItemService.readMenuItem(menuItemId);

        return new ResultResponseDto<>("메뉴 조회 성공", 200, responseDto);
    }

    // 메뉴 ID로 메뉴 수정
    @PutMapping("/{menuItemId}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<Void> updateMenuItem(@PathVariable UUID menuItemId, @RequestBody MenuItemRequestDto requestDto) {

        menuItemService.updateMenuItem(menuItemId, requestDto);

        return new ResultResponseDto<>("메뉴 수정 성공", 200);
    }

    // 메뉴 ID로 메뉴 삭제
    @DeleteMapping("/{menuItemId}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<Void> deleteMenuItem(@PathVariable UUID menuItemId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        menuItemService.deleteMenuItem(menuItemId, userDetails.getUser());

        return new ResultResponseDto<>("메뉴 삭제 성공", 200);
    }

    @DeleteMapping("/all/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<Void> deleteAllMenuItem(@PathVariable UUID restaurantId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        menuItemService.deleteMenuItemByRestaurantId(restaurantId, userDetails.getUser());

        return new ResultResponseDto<>("메뉴 전체 삭제 성공", 200);
    }

}
