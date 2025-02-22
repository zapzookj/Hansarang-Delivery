package com.hansarangdelivery.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansarangdelivery.HansarangDeliveryApplication;
import com.hansarangdelivery.entity.Location;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.entity.UserRole;
import com.hansarangdelivery.jwt.JwtUtil;
import com.hansarangdelivery.repository.LocationRepository;
import com.hansarangdelivery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = HansarangDeliveryApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class LocationAPiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    protected User testUser;
    protected String testUserToken;
    @BeforeEach
    void setUp() {
        locationRepository.deleteAll();

        testUser = new User("testuser", passwordEncoder.encode("Password1!"), "testuser@example.com", UserRole.CUSTOMER);
        userRepository.save(testUser);
        testUserToken = jwtUtil.createToken(testUser.getUsername(), testUser.getRole(), testUser.getId());

        Location location1 = new Location("111111", "서울특별시", "종로구", "세종로", "111111");
        Location location2 = new Location("222222", "서울특별시", "종로구", "서린동", "222222");
        Location location3 = new Location("333333", "서울특별시", "중구", "태평로", "333333");

        locationRepository.save(location1);
        locationRepository.save(location2);
        locationRepository.save(location3);
    }

    @Test
    @DisplayName("Location 검색 API 테스트 - 1")
    void testSearchLocations() throws Exception {
        mockMvc.perform(get("/api/locations")
                .header(JwtUtil.AUTHORIZATION_HEADER, testUserToken)
                .param("page", "1")
                .param("size", "10")
                .param("city", "서울특별시")
                .param("district", "종로구")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("조회 성공")))
            .andExpect(jsonPath("$.statusCode", is(200)))
            .andExpect(jsonPath("$.data.content", hasSize(2)))
            .andExpect(jsonPath("$.data.content[0].city", is("서울특별시")))
            .andExpect(jsonPath("$.data.content[0].district", is("종로구")));
    }

    @Test
    @DisplayName("Location 검색 API 테스트 - 2")
    void testSearchLocations_2() throws Exception {
        mockMvc.perform(get("/api/locations")
                .header(JwtUtil.AUTHORIZATION_HEADER, testUserToken)
                .param("page", "1")
                .param("size", "10")
                .param("city", "서울특별시")
                .param("district", "중구")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("조회 성공")))
            .andExpect(jsonPath("$.statusCode", is(200)))
            .andExpect(jsonPath("$.data.content", hasSize(1)))
            .andExpect(jsonPath("$.data.content[0].city", is("서울특별시")))
            .andExpect(jsonPath("$.data.content[0].district", is("중구")));
    }

    @Test
    @DisplayName("Location 검색 API 테스트 - 3")
    void testSearchLocations_3() throws Exception {
        mockMvc.perform(get("/api/locations")
                .header(JwtUtil.AUTHORIZATION_HEADER, testUserToken)
                .param("page", "1")
                .param("size", "10")
                .param("city", "인천광역시")
                .param("district", "서구")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("조회 성공")))
            .andExpect(jsonPath("$.statusCode", is(200)))
            .andExpect(jsonPath("$.data.content", hasSize(0)));
    }
}
