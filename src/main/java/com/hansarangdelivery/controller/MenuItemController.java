package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.MenuItemRequestDto;
import com.hansarangdelivery.dto.MenuItemResponseDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.entity.MenuItem;
import com.hansarangdelivery.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResultResponseDto> addMenuItem(@RequestBody MenuItemRequestDto requestDto) {
        return menuItemService.addMenuItem(requestDto);
    }

    // 메뉴 ID로 특정 메뉴 조회
    @GetMapping("/{menuItemId}")
    public ResponseEntity<MenuItemResponseDto> searchMenuItem(@PathVariable UUID menuItemId) {
        return menuItemService.searchMenuItem(menuItemId);
    }

    // 메뉴 ID로 메뉴 수정
    @PutMapping("/{menuItemId}")
    public ResponseEntity<ResultResponseDto> updateMenuItem(@PathVariable UUID menuItemId, @RequestBody MenuItemRequestDto requestDto) {
        return menuItemService.updateMenuItem(menuItemId, requestDto);
    }

    // 메뉴 ID로 메뉴 삭제
    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<ResultResponseDto> deleteMenuItem(@PathVariable UUID menuItemId) {
        return menuItemService.updateMenuItem(menuItemId, null);
    }
}
