package com.hansarangdelivery.api;

import com.hansarangdelivery.config.PageableConfig;
import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.global.dto.ResultResponseDto;
import com.hansarangdelivery.menu.dto.MenuItemRequestDto;
import com.hansarangdelivery.menu.dto.MenuItemResponseDto;
import com.hansarangdelivery.menu.dto.MenuItemUpdateDto;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.menu.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<MenuItemResponseDto> createMenuItem(@RequestBody MenuItemRequestDto requestDto) {

        MenuItemResponseDto responseDto = menuItemService.createMenuItem(requestDto);

        return new ResultResponseDto<>("메뉴 저장 성공", 200, responseDto);
    }

    @GetMapping("/restaurant")
    public ResultResponseDto<PageResponseDto<MenuItemResponseDto>> searchAllMenuItem(@RequestParam UUID restaurantId, Pageable pageable) {

        PageableConfig.validatePageSize(pageable);

        PageResponseDto<MenuItemResponseDto> responseDtoList = menuItemService.searchAllMenuItem(restaurantId, pageable);

        return new ResultResponseDto<>("메뉴 조회 성공", 200, responseDtoList);
    }

    // 메뉴 ID로 특정 메뉴 조회
    @GetMapping("/{menuItemId}")
    public ResultResponseDto<MenuItemResponseDto> readMenuItem(@PathVariable UUID menuItemId) {

        MenuItemResponseDto responseDto = menuItemService.readMenuItem(menuItemId);

        return new ResultResponseDto<>("메뉴 조회 성공", 200, responseDto);
    }

    // 메뉴 ID로 메뉴 정보 수정
    @PutMapping("/{menuItemId}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<MenuItemResponseDto> updateMenuItem(@PathVariable UUID menuItemId, @RequestBody MenuItemUpdateDto requestDto) {

        MenuItemResponseDto responseDto = menuItemService.updateMenuItem(menuItemId, requestDto);

        return new ResultResponseDto<>("메뉴 수정 성공", 200, responseDto);
    }

    // 메뉴 ID로 메뉴 숨김 처리 수정
    @PutMapping("/available/{menuItemId}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<MenuItemResponseDto> updateAvailableMenuItem(@PathVariable UUID menuItemId, @RequestParam Boolean isAvailable) {

        MenuItemResponseDto responseDto = menuItemService.updateAvailableMenuItem(menuItemId, isAvailable);

        return new ResultResponseDto<>("메뉴 수정 성공", 200, responseDto);
    }

    // 메뉴 ID로 메뉴 삭제
    @DeleteMapping("/{menuItemId}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<MenuItemResponseDto> deleteMenuItem(@PathVariable UUID menuItemId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MenuItemResponseDto responseDto = menuItemService.deleteMenuItem(menuItemId, userDetails.getUser());

        return new ResultResponseDto<>("메뉴 삭제 성공", 200, responseDto);
    }

    @DeleteMapping("/all/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<List<MenuItemResponseDto>> deleteAllMenuItem(@PathVariable UUID restaurantId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<MenuItemResponseDto> reponseList = menuItemService.deleteMenuItemByRestaurantId(restaurantId, userDetails.getUser());

        return new ResultResponseDto<>("메뉴 전체 삭제 성공", 200, reponseList);
    }
}
