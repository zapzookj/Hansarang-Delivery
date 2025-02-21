package com.hansarangdelivery.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansarangdelivery.HansarangDeliveryApplication;
import com.hansarangdelivery.dto.DeliveryAddressRequestDto;
import com.hansarangdelivery.dto.SignupRequestDto;
import com.hansarangdelivery.dto.UserUpdateDto;
import com.hansarangdelivery.entity.DeliveryAddress;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.entity.UserRole;
import com.hansarangdelivery.jwt.JwtUtil;
import com.hansarangdelivery.repository.DeliveryAddressRepository;
import com.hansarangdelivery.repository.UserRepository;
import com.hansarangdelivery.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = HansarangDeliveryApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class UserApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DeliveryAddressRepository deliveryAddressRepository;

    @MockitoBean
    private LocationService locationService;

    protected User testUser;
    protected User testUser2;
    protected User manager;
    protected User master;
    protected String testUserToken;
    protected String testUser2Token;
    protected String managerToken;
    protected String masterToken;
    protected DeliveryAddress testAddress;
    protected UUID testAddressId;
    protected UUID locationId;


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = new User("testuser", passwordEncoder.encode("Password1!"), "testuser@example.com", UserRole.CUSTOMER);
        userRepository.save(testUser);
        testUserToken = jwtUtil.createToken(testUser.getUsername(), testUser.getRole());

        testUser2 = new User("testuser2", passwordEncoder.encode("Password1!"), "testuser2@example.com", UserRole.CUSTOMER);
        userRepository.save(testUser2);
        testUser2Token = jwtUtil.createToken(testUser.getUsername(), testUser.getRole());

        manager = new User("manager", passwordEncoder.encode("Password1!"), "manager@example.com", UserRole.MANAGER);
        userRepository.save(manager);
        managerToken = jwtUtil.createToken(manager.getUsername(), manager.getRole());

        master = new User("master", passwordEncoder.encode("Password1!"), "master@example.com", UserRole.MASTER);
        userRepository.save(master);
        masterToken = jwtUtil.createToken(master.getUsername(), master.getRole());
    }

    // 배송지를 생성하는 메서드 (기본 배송지 설정 로직 테스트용)
    private void createDeliveryAddress(String token, UUID locationId, String requestMessage, boolean isDefault) throws Exception {
        when(locationService.existsById(ArgumentMatchers.eq(locationId))).thenReturn(true); // locationId는 유효하다고 가정

        DeliveryAddressRequestDto requestDto = new DeliveryAddressRequestDto();
        requestDto.setLocationId(locationId);
        requestDto.setRequestMessage(requestMessage);
        requestDto.setIsDefault(isDefault);

        mockMvc.perform(post("/api/users/delivery-addresses")
                .header(JwtUtil.AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("배송지 추가 완료"));
    }

    // ====================== User API 테스트 ======================

    @Test
    @DisplayName("유저 회원 가입 API 테스트")
    void testSignup() throws Exception {
        // Given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername("newuser");
        requestDto.setPassword("Password1!"); // 패스워드 규칙에 맞는 값
        requestDto.setEmail("newuser@example.com"); // email 규칙에 맞는 값
        requestDto.setRole(null);  // 입력하지 않으면 기본 CUSTOMER
        requestDto.setAdminCode(""); // MASTER 등록 시에만 사용

        // When & Then
        mockMvc.perform(post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("회원가입 성공"));

        Optional<User> user = userRepository.findByUsername("newuser");
        assertTrue(user.isPresent());
        assertEquals("newuser@example.com", user.get().getEmail());
        assertEquals(UserRole.CUSTOMER, user.get().getRole());
    }

    @Test
    @DisplayName("내 유저 정보 단건 조회 API 테스트")
    void testReadMyProfile() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/users/my-profile")
                .header(JwtUtil.AUTHORIZATION_HEADER, testUserToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.username").value("testuser"))
            .andExpect(jsonPath("$.data.email").value("testuser@example.com"));
    }

    @Test
    @DisplayName("특정 유저 정보 단건 조회 API 테스트 (매니저 전용)")
    void testReadProfile() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/users/" + testUser.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, managerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.username").value("testuser"))
            .andExpect(jsonPath("$.data.email").value("testuser@example.com"));
    }

    @Test
    @DisplayName("유저 정보 전체 조회 API 테스트 (매니저 전용)")
    void testSearchProfile() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/users")
                .header(JwtUtil.AUTHORIZATION_HEADER, managerToken)
                .param("page", "0")
                .param("size", "10")
                .param("isAsc", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.content").isArray())
            .andExpect(jsonPath("$.data.content.length()").value(greaterThanOrEqualTo(4)));
    }

    @Test
    @DisplayName("내 유저 정보 수정 API 테스트")
    void testUpdateProfile() throws Exception {
        // Given
        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setUsername("updateduser");
        updateDto.setEmail("updatedemail@example.com");

        // When & Then
        mockMvc.perform(put("/api/users/my-profile")
                .header(JwtUtil.AUTHORIZATION_HEADER, testUserToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("회원 정보 수정 성공"));

        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertEquals("updateduser", updatedUser.getUsername());
        assertEquals("updatedemail@example.com", updatedUser.getEmail());
    }

    @Test
    @DisplayName("특정 유저 권한 변경 API 테스트")
    void testUpdateRole() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/users/" + testUser.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, masterToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("권한 변경 성공"));

        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertEquals(UserRole.MANAGER, updatedUser.getRole());
    }

    @Test
    @DisplayName("본인 유저 삭제(탈퇴) API 테스트")
    void testDeleteUser1() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/users")
                .header(JwtUtil.AUTHORIZATION_HEADER, testUserToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("회원 탈퇴 성공"));

        assertFalse(userRepository.findById(testUser.getId()).isPresent());
    }

    @Test
    @DisplayName("특정 유저 삭제(탈퇴) API 테스트")
    void testDeleteUser2() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/users")
                .header(JwtUtil.AUTHORIZATION_HEADER, managerToken)
                .param("userId", testUser.getId().toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("회원 탈퇴 성공"));

        assertFalse(userRepository.findById(testUser.getId()).isPresent());
    }

    // ====================== DeliveryAddress(배송지) API 테스트 ======================

    @Test
    @DisplayName("배송지 생성 API 테스트")
    void testCreateDeliveryAddress() throws Exception {
        // Given
        UUID locationId = UUID.randomUUID();
        when(locationService.existsById(ArgumentMatchers.eq(locationId))).thenReturn(true); // locationId는 유효하다고 가정

        DeliveryAddressRequestDto requestDto = new DeliveryAddressRequestDto();
        requestDto.setLocationId(locationId);
        requestDto.setRequestMessage("문 앞에 놔주세요.");
        requestDto.setIsDefault(true);

        // When & Then
        mockMvc.perform(post("/api/users/delivery-addresses")
                .header(JwtUtil.AUTHORIZATION_HEADER, testUserToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("배송지 추가 완료"));

        List<DeliveryAddress> addresses = deliveryAddressRepository.findAll();
        assertFalse(addresses.isEmpty());
    }

    @Test
    @DisplayName("자신의 기본 배송지 조회 API 테스트")
    void testReadMyDeliveryAddress() throws Exception {
        // Given
        UUID locationId = UUID.randomUUID();
        createDeliveryAddress(testUserToken, locationId, "test", false);


        // When & Then
        mockMvc.perform(get("/api/users/delivery-addresses/default")
                .header(JwtUtil.AUTHORIZATION_HEADER, testUserToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.locationId").value(locationId.toString()))
            .andExpect(jsonPath("$.data.requestMessage").value("test"))
            .andExpect(jsonPath("$.data.isDefault").value(true)); // 첫 배송지는 기본 배송지로 설정
    }

    @Test
    @DisplayName("자신의 배송지 전체 조회 API 테스트")
    void testSearchMyDeliveryAddresses() throws Exception {
        // Given
        UUID locationId1 = UUID.randomUUID();
        UUID locationId2 = UUID.randomUUID();
        createDeliveryAddress(testUserToken, locationId1, "test1", true);
        createDeliveryAddress(testUserToken, locationId2, "test2", false);

        // When & Then
        mockMvc.perform(get("/api/users/delivery-addresses")
                .header(JwtUtil.AUTHORIZATION_HEADER, testUserToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    @DisplayName("특정 유저의 기본 배송지 조회 API 테스트")
    void testReadDeliveryAddress() throws Exception {
        // Given
        UUID locationId = UUID.randomUUID();
        createDeliveryAddress(testUserToken, locationId, "test", true);

        // When & Then
        mockMvc.perform(get("/api/users/" + testUser.getId() + "/delivery-addresses/default")
                .header(JwtUtil.AUTHORIZATION_HEADER, managerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.locationId").value(locationId.toString()))
            .andExpect(jsonPath("$.data.requestMessage").value("test"))
            .andExpect(jsonPath("$.data.isDefault").value(true));
    }

    @Test
    @DisplayName("특정 유저의 배송지 전체 조회 API 테스트")
    void testSearchDeliveryAddresses() throws Exception {
        // Given
        UUID locationId1 = UUID.randomUUID();
        UUID locationId2 = UUID.randomUUID();
        createDeliveryAddress(testUserToken, locationId1, "test1", true);
        createDeliveryAddress(testUserToken, locationId2, "test2", false);

        // When & Then
        mockMvc.perform(get("/api/users/" + testUser.getId() + "/delivery-addresses")
                .header(JwtUtil.AUTHORIZATION_HEADER, managerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    @DisplayName("배송지 수정 API 테스트")
    void testUpdateDeliveryAddress() throws Exception {
        // Given
        UUID locationId = UUID.randomUUID();
        createDeliveryAddress(testUserToken, locationId, "test", true);

        List<DeliveryAddress> addresses = deliveryAddressRepository.findAll();
        assertFalse(addresses.isEmpty());
        DeliveryAddress address = addresses.get(0);

        UUID newLocationId = UUID.randomUUID();
        when(locationService.existsById(ArgumentMatchers.eq(newLocationId))).thenReturn(true);

        DeliveryAddressRequestDto updateDto = new DeliveryAddressRequestDto();
        updateDto.setLocationId(newLocationId);
        updateDto.setRequestMessage("update");
        updateDto.setIsDefault(true);

        // When & Then
        mockMvc.perform(put("/api/users/delivery-addresses/" + address.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, testUserToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("배송지 정보 수정 완료"));

        DeliveryAddress updatedAddress = deliveryAddressRepository.findById(address.getId()).orElseThrow();
        assertEquals(newLocationId, updatedAddress.getLocationId());
        assertEquals("update", updatedAddress.getRequestMessage());
        assertTrue(updatedAddress.getIsDefault());
    }

    @Test
    @DisplayName("배송지 삭제 API 테스트")
    void testDeleteDeliveryAddress() throws Exception {
        UUID locationId = UUID.randomUUID();
        createDeliveryAddress(testUserToken, locationId, "test", true);

        List<DeliveryAddress> addresses = deliveryAddressRepository.findAll();
        assertFalse(addresses.isEmpty());
        DeliveryAddress address = addresses.get(0);

        mockMvc.perform(delete("/api/users/delivery-addresses/" + address.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, testUserToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("배송지 삭제 완료"));

        assertFalse(deliveryAddressRepository.findById(address.getId()).isPresent());
    }
}
