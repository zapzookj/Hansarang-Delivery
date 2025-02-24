package com.hansarangdelivery.menu.service;

import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.global.exception.ResourceNotFoundException;
import com.hansarangdelivery.menu.dto.MenuItemRequestDto;
import com.hansarangdelivery.menu.dto.MenuItemResponseDto;
import com.hansarangdelivery.menu.dto.MenuItemUpdateDto;
import com.hansarangdelivery.restaurant.model.Restaurant;
import com.hansarangdelivery.restaurant.repository.RestaurantRepository;
import com.hansarangdelivery.security.jwt.JwtUtil;
import com.hansarangdelivery.user.model.User;
import com.hansarangdelivery.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:application-test-local.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
class MenuItemServiceTest {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    MenuItemService menuItemService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    User owner;

    Restaurant restaurant;
    UUID restaurantId = UUID.fromString("66177930-5132-400c-9cd0-6e027beaf620");

    MenuItemResponseDto createMenuItem = null;

    String jwtToken;

    @BeforeAll
    @Transactional
    public void setUp() {

        owner = userRepository.findById(1L).orElse(null);

        if (owner != null) {
            jwtToken = jwtUtil.createToken(owner.getUsername(), owner.getRole(), owner.getId());
        }

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                owner.getId(),
                owner.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(owner.getRole().name()))
            );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        restaurant = restaurantRepository.findById(restaurantId).orElse(null);
    }

    @Test
    @Order(1)
    @DisplayName("신규 메뉴 등록")
    void createMenuItem() {
        // given
        String name = "황금올리브 양념";
        int price = 24_000;

        // when
        MenuItemRequestDto requestDto = new MenuItemRequestDto(name, price, restaurantId, null);

        MenuItemResponseDto menuItem = menuItemService.createMenuItem(requestDto);

        // then
        assertNotNull(menuItem.getId());
        assertEquals(name, menuItem.getName());
        assertEquals(price, menuItem.getPrice());

        createMenuItem = menuItem;
    }

    @Test
    @Order(2)
    @DisplayName("식당 전체메뉴 조회")
    void searchAllMenuItem() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        PageResponseDto<MenuItemResponseDto> menuItemsResponse = menuItemService.searchAllMenuItem(restaurantId, pageable);

        UUID createdMenuItemId = this.createMenuItem.getId();

        List<MenuItemResponseDto> menuItems = menuItemsResponse.getContent();

        MenuItemResponseDto foundMenuItem = menuItems.stream()
            .filter(menuItem -> menuItem.getId().equals(createdMenuItemId))
            .findFirst()
            .orElse(null);

        assertNotNull(foundMenuItem);
        assertEquals(createMenuItem.getName(), foundMenuItem.getName());
        assertEquals(createMenuItem.getPrice(), foundMenuItem.getPrice());
    }

    @Test
    @Order(3)
    @DisplayName("식당 메뉴 단일 조회")
    void readMenuItem() {

        UUID createdMenuItemId = this.createMenuItem.getId();

        MenuItemResponseDto foundMenuItem = menuItemService.readMenuItem(createdMenuItemId);

        assertNotNull(foundMenuItem);
        assertEquals(createMenuItem.getName(), foundMenuItem.getName());
        assertEquals(createMenuItem.getPrice(), foundMenuItem.getPrice());
    }

    @Test
    @Order(4)
    @DisplayName("식당 메뉴 수정")
    void updateMenuItem() {

        // 메뉴의 가격 변경
        MenuItemUpdateDto requestDto = new MenuItemUpdateDto("황금올리브 양념 콤보", 23500);

        MenuItemResponseDto menuItem = menuItemService.updateMenuItem(createMenuItem.getId(), requestDto);

        assertNotEquals(createMenuItem.getName(), menuItem.getName());
        assertNotEquals(createMenuItem.getPrice(), menuItem.getPrice());
    }

    @Test
    @Order(5)
    @DisplayName("식당 메뉴 삭제")
    void deleteMenuItem() {

        menuItemService.deleteMenuItem(createMenuItem.getId(), owner);

        assertThrows(ResourceNotFoundException.class, () -> {
            menuItemService.readMenuItem(createMenuItem.getId());
        });
    }
}