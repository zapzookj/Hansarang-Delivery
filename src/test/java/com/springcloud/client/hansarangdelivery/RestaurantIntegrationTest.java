package com.springcloud.client.hansarangdelivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansarangdelivery.HansarangDeliveryApplication;
import com.hansarangdelivery.dto.RestaurantRequestDto;
import com.hansarangdelivery.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = HansarangDeliveryApplication.class)
@AutoConfigureMockMvc
@Transactional
class RestaurantIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        restaurantRepository.deleteAll();
//        TODO :매번 로그인해서 jwtToken에 토큰 값 넣어서 아래 테스트 에서 사용해야하나 확인
//          MOckUser로 안됨 -> jwtToken용 간단하게 테스트에서 사용할 어노테이션 있나 확인
    }

    @Test
    @DisplayName("restaurant search API test")
//    로그인한 유저의 jwt사용 부분
    void searchRestaurants() throws Exception {
        // Given
        createTestRestaurant();

        // When & Then
        mockMvc.perform(get("/api/restaurants/search")
                .header("Authorization",
                    "Bearer {이부분에 들어갈 내용 포스트맨으로 직접 로그인해보고 토큰 값 하드코딩했음}")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.content[0].name").value("테스트 음식점"));
    }

    private UUID createTestRestaurant() {
        var restaurant = new com.hansarangdelivery.entity.Restaurant(
            "테스트 음식점", UUID.randomUUID(), 1L, UUID.randomUUID());
        restaurantRepository.save(restaurant);
        entityManager.flush();
        entityManager.clear();
        return restaurant.getId();
    }
}
