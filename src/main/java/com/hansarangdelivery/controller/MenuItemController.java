package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.MenuItemRequestDto;
import com.hansarangdelivery.dto.MenuItemResponseDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.entity.MenuItem;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuItemController {

    private final MenuItemService menuItemService;

    // 메뉴 추가
    @PostMapping("")
    public ResultResponseDto<Void> addMenuItem(@RequestBody MenuItemRequestDto requestDto) {

        menuItemService.addMenuItem(requestDto);

        return new ResultResponseDto<>("리뷰 저장 성공", 200);
    }

    @GetMapping("/all/{restaurantId}")
    public ResultResponseDto<List<MenuItemResponseDto>> searchAllMenuItem(@PathVariable UUID restaurantId) {

        List<MenuItemResponseDto> responseDtoList = menuItemService.searchAllMenuItem(restaurantId);

        return new ResultResponseDto<>("리뷰 조회 성공", 200, responseDtoList);
    }

    // 메뉴 ID로 특정 메뉴 조회
    @GetMapping("/{menuItemId}")
    public ResultResponseDto<MenuItemResponseDto> searchMenuItem(@PathVariable UUID menuItemId) {

        MenuItemResponseDto responseDto = menuItemService.searchMenuItem(menuItemId);

        return new ResultResponseDto<>("리뷰 조회 성공", 200, responseDto);
    }

    // 메뉴 ID로 메뉴 수정
    @PutMapping("/{menuItemId}")
    public ResultResponseDto<Void> updateMenuItem(@PathVariable UUID menuItemId, @RequestBody MenuItemRequestDto requestDto) {

        menuItemService.updateMenuItem(menuItemId, requestDto);

        return new ResultResponseDto<>("리뷰 수정 성공", 200);
    }

    // 메뉴 ID로 메뉴 삭제
    @DeleteMapping("/{menuItemId}")
    public ResultResponseDto<Void> deleteMenuItem(@PathVariable UUID menuItemId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        menuItemService.deleteMenuItem(menuItemId, userDetails.getUser());

        return new ResultResponseDto<>("리뷰 삭제 성공", 200);
    }

    @DeleteMapping("/all/{restaurantId}")
    public ResultResponseDto<Void> deleteAllMenuItem(@PathVariable UUID restaurantId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        menuItemService.deleteMenuItemByRestaurantId(restaurantId, userDetails.getUser());

        return new ResultResponseDto<>("리뷰 삭제 성공", 200);
    }

}
