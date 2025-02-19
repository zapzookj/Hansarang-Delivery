package com.springcloud.client.hansarangdelivery.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansarangdelivery.HansarangDeliveryApplication;
import com.hansarangdelivery.dto.SignupRequestDto;
import com.hansarangdelivery.dto.UserUpdateDto;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.entity.UserRole;
import com.hansarangdelivery.jwt.JwtUtil;
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

import java.util.Optional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.*;
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
    @BeforeEach
    void setUp() {
        // 각 테스트 전 DB 초기화
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저 회원 가입 API 테스트")
    void testSignup() throws Exception {
        // Given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername("testuser");
        requestDto.setPassword("Password1!"); // 패스워드 규칙에 맞는 값
        requestDto.setEmail("testuser@example.com"); // email 규칙에 맞는 값
        requestDto.setRole(null);  // 입력하지 않으면 기본 CUSTOMER
        requestDto.setAdminCode(""); // MASTER 등록 시에만 사용

        // When & Then
        mockMvc.perform(post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("회원가입 성공"));

        Optional<User> user = userRepository.findByUsername("testuser");
        assertTrue(user.isPresent());
        assertEquals("testuser@example.com", user.get().getEmail());
        assertEquals(UserRole.CUSTOMER, user.get().getRole());
    }

    @Test
    @DisplayName("내 유저 정보 단건 조회 API 테스트")
    void testReadMyProfile() throws Exception {
        // Given
        User user = new User("testuser", passwordEncoder.encode("Password1!"), "testuser@example.com", UserRole.CUSTOMER);
        userRepository.save(user);
        String token = jwtUtil.createToken(user.getUsername(), user.getRole());

        // When & Then
        mockMvc.perform(get("/api/users/my-profile")
                .header(JwtUtil.AUTHORIZATION_HEADER, token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.username").value("testuser"))
            .andExpect(jsonPath("$.data.email").value("testuser@example.com"));
    }

    @Test
    @DisplayName("특정 유저 정보 단건 조회 API 테스트 (매니저 전용)")
    void testReadProfile() throws Exception {
        // Given
        User customerUser = new User("customer", passwordEncoder.encode("Password1!"), "customer@example.com", UserRole.CUSTOMER);
        userRepository.save(customerUser);

        User managerUser = new User("manager", passwordEncoder.encode("Password1!"), "manager@example.com", UserRole.MANAGER);
        userRepository.save(managerUser);
        String managerToken = jwtUtil.createToken(managerUser.getUsername(), managerUser.getRole());

        // When & Then
        mockMvc.perform(get("/api/users/" + customerUser.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, managerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.username").value("customer"))
            .andExpect(jsonPath("$.data.email").value("customer@example.com"));
    }

    @Test
    @DisplayName("유저 정보 전체 조회 API 테스트 (매니저 전용)")
    void testSearchProfile() throws Exception {
        // Given
        User user1 = new User("user1", passwordEncoder.encode("Password1!"), "user1@example.com", UserRole.CUSTOMER);
        User user2 = new User("user2", passwordEncoder.encode("Password1!"), "user2@example.com", UserRole.CUSTOMER);
        userRepository.save(user1);
        userRepository.save(user2);

        User managerUser = new User("manager", passwordEncoder.encode("Password1!"), "manager@example.com", UserRole.MANAGER);
        userRepository.save(managerUser);
        String managerToken = jwtUtil.createToken(managerUser.getUsername(), managerUser.getRole());

        // When & Then
        mockMvc.perform(get("/api/users")
                .header(JwtUtil.AUTHORIZATION_HEADER, managerToken)
                .param("page", "1")
                .param("size", "10")
                .param("isAsc", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.content").isArray())
            .andExpect(jsonPath("$.data.content.length()").value(greaterThanOrEqualTo(3)));
    }

    @Test
    @DisplayName("내 유저 정보 수정 API 테스트")
    void testUpdateProfile() throws Exception {
        // Given
        User user = new User("testuser", passwordEncoder.encode("Password1!"), "testemail@example.com", UserRole.CUSTOMER);
        userRepository.save(user);
        String token = jwtUtil.createToken(user.getUsername(), user.getRole());

        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setUsername("updateduser");
        updateDto.setEmail("updatedemail@example.com");

        // When & Then
        mockMvc.perform(put("/api/users/my-profile")
                .header(JwtUtil.AUTHORIZATION_HEADER, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("회원 정보 수정 성공"));

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals("updateduser", updatedUser.getUsername());
        assertEquals("updatedemail@example.com", updatedUser.getEmail());
    }

    @Test
    @DisplayName("특정 유저 권한 변경 API 테스트")
    void testUpdateRole() throws Exception {
        // Given
        User targetUser = new User("testuser", passwordEncoder.encode("Password1!"), "testuser@example.com", UserRole.CUSTOMER);
        userRepository.save(targetUser);

        User masterUser = new User("master", passwordEncoder.encode("Password1!"), "master@example.com", UserRole.MASTER);
        userRepository.save(masterUser);
        String masterToken = jwtUtil.createToken(masterUser.getUsername(), masterUser.getRole());

        // When & Then
        mockMvc.perform(put("/api/users/" + targetUser.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, masterToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("권한 변경 성공"));

        User updatedUser = userRepository.findById(targetUser.getId()).orElseThrow();
        assertEquals(UserRole.MANAGER, updatedUser.getRole());
    }

    @Test
    @DisplayName("본인 유저 삭제(탈퇴) API 테스트")
    void testDeleteUser1() throws Exception {
        // Given
        User user = new User("testuser", passwordEncoder.encode("Password1!"), "testuser@example.com", UserRole.CUSTOMER);
        userRepository.save(user);
        String token = jwtUtil.createToken(user.getUsername(), user.getRole());

        // When & Then
        mockMvc.perform(delete("/api/users")
                .header(JwtUtil.AUTHORIZATION_HEADER, token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("회원 탈퇴 성공"));

        assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    @DisplayName("특정 유저 삭제(탈퇴) API 테스트")
    void testDeleteUser2() throws Exception {
        // Given
        User targetUser = new User("testuser", passwordEncoder.encode("Password1!"), "testuser@example.com", UserRole.CUSTOMER);
        userRepository.save(targetUser);

        User masterUser = new User("master", passwordEncoder.encode("Password1!"), "master@example.com", UserRole.MASTER);
        userRepository.save(masterUser);
        String masterToken = jwtUtil.createToken(masterUser.getUsername(), masterUser.getRole());

        // When & Then
        mockMvc.perform(delete("/api/users")
                .header(JwtUtil.AUTHORIZATION_HEADER, masterToken)
                .param("userId", targetUser.getId().toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("회원 탈퇴 성공"));

        assertFalse(userRepository.findById(targetUser.getId()).isPresent());
    }
}
