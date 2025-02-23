package com.hansarangdelivery.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansarangdelivery.HansarangDeliveryApplication;
import com.hansarangdelivery.dto.RestaurantRequestDto;
import com.hansarangdelivery.entity.Category;
import com.hansarangdelivery.entity.Location;
import com.hansarangdelivery.entity.Restaurant;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.entity.UserRole;
import com.hansarangdelivery.jwt.JwtUtil;
import com.hansarangdelivery.repository.CategoryRepository;
import com.hansarangdelivery.repository.LocationRepository;
import com.hansarangdelivery.repository.RestaurantRepository;
import com.hansarangdelivery.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = HansarangDeliveryApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
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
    private UserDetailsService userDetailsService;

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
    @DisplayName("restaurant register API test")
    void registerRestaurant() throws Exception {
        // Given
        RestaurantRequestDto requestDto = new RestaurantRequestDto();
        requestDto.setName("통합 테스트 음식점");
        requestDto.setCategory_id(createTestCategory());
        requestDto.setOwner_id(getOwnerUUID());
        requestDto.setLocation_id(createTestLocation());
        requestDto.setOpen(true);

        String jwtToken = getJwtTokenByManager();

        // When & Then
        mockMvc.perform(post(url)
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("가게 등록 성공"))
            .andExpect(jsonPath("$.data.name").value("통합 테스트 음식점"));
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
    @DisplayName("restaurant update API test")
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
            .andExpect(jsonPath("$.message").value("가게 수정 성공"))
            .andExpect(jsonPath("$.data.name").value("닫은 음식점"));
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
        mockMvc.perform(get(url)
                .header("Authorization",jwtToken)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.content[0].name").value("테스트 음식점"));
    }

    private UUID createTestRestaurant() {
        Location location = new Location("1111010100","서울특별시","종로구","청운동","123456" );
        locationRepository.save(location);

        Category category = new Category("테스트 카테고리");
        categoryRepository.save(category);


        User user = new User("searchCustomer","1111","searchCustomer@mail.com",UserRole.OWNER);
        userRepository.save(user);

        entityManager.flush();
        entityManager.clear();

        UUID locationId = location.getId();
        UUID categoryId = category.getId();
        Long ownerId = user.getId();

        Restaurant restaurant = new Restaurant(
            "테스트 음식점", categoryId, ownerId,locationId);
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
        locationRepository.save(location);
        entityManager.flush();
        entityManager.clear();
        return location.getId();
    }

    private String getJwtTokenByCustomer(){
        String jwtToken;
        User user = new User("customer1","1111","customer1@mail.com", UserRole.CUSTOMER);
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        // 인증 객체 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(user.getUsername());
        context.setAuthentication(authentication);

        jwtToken = jwtUtil.createToken("customer1",UserRole.CUSTOMER, user.getId());
        return jwtToken;
    }
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private Long getOwnerUUID(){
        User user = new User("owner","1111","owner@mail.com",UserRole.OWNER);

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

        // 인증 객체 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(user.getUsername());
        context.setAuthentication(authentication);

        jwtToken = jwtUtil.createToken("manager1",UserRole.MANAGER, user.getId());
        return jwtToken;
    }
}
