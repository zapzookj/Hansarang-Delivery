package com.hansarangdelivery.review.service;

import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.global.exception.ForbiddenActionException;
import com.hansarangdelivery.global.exception.ResourceNotFoundException;
import com.hansarangdelivery.review.dto.ReviewRequestDto;
import com.hansarangdelivery.review.dto.ReviewResponseDto;
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
class ReviewServiceTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserRepository userRepository;

    User customer;
    String customerToken;

    User owner;
    String ownerToken;

    User manager;
    String managerToken;

    UUID completedOrderId = UUID.fromString("1c51b309-c217-4594-aeda-747c773d4857");
    UUID canceledOrderId = UUID.fromString("1e5a7088-cf73-4b69-9ff7-d1cb59652e44");

    UUID restaurantId = UUID.fromString("66177930-5132-400c-9cd0-6e027beaf620");

    ReviewResponseDto createdReview;
    UUID reviewId;

    @BeforeAll
    @Transactional
    public void setup() {

        customer = userRepository.findById(4L).orElse(null);
        customerToken = jwtUtil.createToken(customer.getUsername(), customer.getRole(), customer.getId());

        owner = userRepository.findById(1L).orElse(null);
        ownerToken = jwtUtil.createToken(owner.getUsername(), owner.getRole(), owner.getId());

        manager = userRepository.findById(3L).orElse(null);
        managerToken = jwtUtil.createToken(manager.getUsername(), manager.getRole(), manager.getId());
    }

    @Test
    @Order(1)
    @DisplayName("리뷰 생성")
    void createReview() {

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                customer.getId(),
                customer.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(customer.getRole().name()))
            );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String content = "맛있어요 굿굿";
        int rating = 5;

        ReviewRequestDto reviewRequestDto = new ReviewRequestDto(completedOrderId, restaurantId, content, rating);

        createdReview = reviewService.createReview(reviewRequestDto);

        reviewId = createdReview.getId();

        assertNotNull(createdReview);

        assertEquals(completedOrderId, createdReview.getOrderId());
        assertEquals(content, createdReview.getContent());
        assertEquals(rating, createdReview.getRating());
    }

    @Test
    @Order(2)
    @DisplayName("리뷰 작성 실패[완료되지 않은 주문]")
    void createReviewFailed() {

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                customer.getId(),
                customer.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(customer.getRole().name()))
            );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String content = "배달이 너무 늦어요";
        int rating = 2;

        ReviewRequestDto requestDto = new ReviewRequestDto(canceledOrderId, restaurantId, content, rating);

        assertThrows(ForbiddenActionException.class, () -> {
            createdReview = reviewService.createReview(requestDto);
        });
    }

    @Test
    @Order(3)
    @DisplayName("식당 리뷰 조회")
    void searchRestaurantReview() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        PageResponseDto<ReviewResponseDto> reviewPages = reviewService.searchRestaurantReview(restaurantId, pageable);

        List<ReviewResponseDto> reviewList = reviewPages.getContent();

        ReviewResponseDto foundMenuItem = reviewList.stream()
            .filter(menuItem -> menuItem.getId().equals(reviewId))
            .findFirst()
            .orElse(null);

        assertNotNull(foundMenuItem);

        assertEquals(reviewId, foundMenuItem.getId());
        assertEquals(createdReview.getCreatedBy(), foundMenuItem.getCreatedBy());
        assertEquals(createdReview.getContent(), foundMenuItem.getContent());
        assertEquals(createdReview.getRating(), foundMenuItem.getRating());
    }

    @Test
    @Order(4)
    @DisplayName("내가 작성한 리뷰 조회")
    void searchMyReview() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        PageResponseDto<ReviewResponseDto> reviewPages = reviewService.searchMyReview(customer.getId(), pageable);

        List<ReviewResponseDto> reviewList = reviewPages.getContent();

        ReviewResponseDto foundMenuItem = reviewList.stream()
            .filter(menuItem -> menuItem.getId().equals(reviewId))
            .findFirst()
            .orElse(null);

        assertNotNull(foundMenuItem);

        assertEquals(reviewId, foundMenuItem.getId());
        assertEquals(createdReview.getCreatedBy(), foundMenuItem.getCreatedBy());
        assertEquals(createdReview.getContent(), foundMenuItem.getContent());
        assertEquals(createdReview.getRating(), foundMenuItem.getRating());
    }

    @Test
    @Order(5)
    @DisplayName("리뷰 수정")
    void updateReview() {

        String content = "리뷰 수정했어요.";
        int rating = 4;

        ReviewRequestDto requestDto = new ReviewRequestDto(content, rating);

        ReviewResponseDto updatedReview = reviewService.updateReview(reviewId, requestDto, customer);

        assertNotNull(updatedReview);

        assertEquals(createdReview.getCreatedBy(), updatedReview.getCreatedBy());
        assertNotEquals(createdReview.getContent(), updatedReview.getContent());
        assertNotEquals(createdReview.getRating(), updatedReview.getRating());
    }

    @Test
    @Order(6)
    @DisplayName("리뷰 삭제 실패[권한 없는 사용자의 요청]")
    void deleteReviewFailed() {

        assertThrows(ForbiddenActionException.class, () -> {
            reviewService.deleteReview(reviewId, owner);
        });
    }

    @Test
    @Order(7)
    @DisplayName("리뷰 삭제 성공")
    void deleteReview() {

        reviewService.deleteReview(reviewId, customer);

        assertThrows(ResourceNotFoundException.class, () -> {
            reviewService.readReview(reviewId);
        });
    }
}