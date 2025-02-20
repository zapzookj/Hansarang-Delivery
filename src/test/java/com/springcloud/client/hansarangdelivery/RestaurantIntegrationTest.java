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
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();
        locationRepository.deleteAll();
    }

    private String url = "/api/restaurants";


    @Test
    @DisplayName("가게 등록 API 테스트")
    void registerRestaurant() throws Exception {
        // Given
        RestaurantRequestDto requestDto = new RestaurantRequestDto();
        requestDto.setName("통합 테스트 음식점");
        requestDto.setCategory_id(createTestCategory());
        requestDto.setOwner_id(getUserUUID());
        requestDto.setLocation_id(createTestLocation());
        requestDto.setOpen(true);

        String jwtToken = getJwtTokenByManager();

        // When & Then
        mockMvc.perform(post(url)
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("가게 등록 성공"));
//            .andExpect(jsonPath("$.data.name").value("통합 테스트 음식점"));
    }

    @Test
    @DisplayName("가게 조회 API 테스트")
    void getRestaurantInfo() throws Exception {
        // Given: 사전 데이터 삽입
        UUID restaurantId = createTestRestaurant();
        String jwtToken = getJwtTokenByManager();
        // When & Then
        mockMvc.perform(get(url + "/"+restaurantId)
                .header("Authorization", jwtToken)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.name").value("테스트 음식점"));
    }

    @Test
    @DisplayName("가게 수정 API 테스트")
    void updateRestaurant() throws Exception {
        // Given
        UUID restaurantId = createTestRestaurant();
        String jwtToken = getJwtTokenByManager();

        RestaurantRequestDto updateDto = new RestaurantRequestDto();
        updateDto.setName("닫은 음식점");
        updateDto.setOpen(false);

        // When & Then
        mockMvc.perform(put(url + "/"+restaurantId)
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("가게 수정 성공"));
    }

    @Test
    @DisplayName("restaurant delete API test")
    void deactivateRestaurant() throws Exception {
        // Given
        UUID restaurantId = createTestRestaurant();
        String jwtToken = getJwtTokenByManager();
        // When & Then
        mockMvc.perform(delete(url + "/"+restaurantId)
                .header("Authorization", jwtToken)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("가게 삭제 성공"));
    }

    @Test
    @DisplayName("restaurant search API test")

    void searchRestaurants() throws Exception {
        // Given
        createTestRestaurant();
        String jwtToken = getJwtTokenByCustomer();

        // When & Then
        mockMvc.perform(get(url+"/search")
                .header("Authorization",jwtToken)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.content[0].name").value("테스트 음식점"));
    }

    private UUID createTestRestaurant() {

        Restaurant restaurant = new com.hansarangdelivery.entity.Restaurant(
            "테스트 음식점", UUID.randomUUID(), 10480L, UUID.randomUUID());
        restaurantRepository.save(restaurant);
        entityManager.flush();
        entityManager.clear();
        return restaurant.getId();
    }

    private UUID createTestCategory() {
        Category category = new Category("테스트 카테고리");
        categoryRepository.save(category);
        entityManager.flush();
        entityManager.clear();
        return category.getId();
    }

    private UUID createTestLocation() {
        Location location = new Location("1111010100","서울특별시","종로구","청운동","123456" );

        System.out.println(location.getLawCode());
        System.out.println(location.getCity());
        System.out.println(location.getDistrict());

        locationRepository.save(location);
        entityManager.flush();
        entityManager.clear();
        return location.getId();
    }

    private String getJwtTokenByCustomer(){
        String jwtToken;
        User user = new User("customer1","1111","customer1@mail.com",UserRole.CUSTOMER);
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();
        jwtToken = jwtUtil.createToken("customer1",UserRole.CUSTOMER);
        return jwtToken;
    }

    private Long getUserUUID(){
        User user = new User("customer1","1111","customer1@mail.com",UserRole.CUSTOMER);
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();
        return user.getId();
    }

    private String getJwtTokenByManager(){
        String jwtToken;
        User user = new User("manager1","1111","manager1@mail.com",UserRole.MANAGER);
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();
        jwtToken = jwtUtil.createToken("manager1",UserRole.MANAGER);
        return jwtToken;
    }
}
