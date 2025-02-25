package com.hansarangdelivery.aiResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansarangdelivery.HansarangDeliveryApplication;
import com.hansarangdelivery.ai.dto.AiApiResponseDto;
import com.hansarangdelivery.ai.dto.AiRequestDto;
import com.hansarangdelivery.ai.model.AiResponse;
import com.hansarangdelivery.user.model.User;
import com.hansarangdelivery.user.model.UserRole;
import com.hansarangdelivery.security.jwt.JwtUtil;
import com.hansarangdelivery.ai.repository.AiResponseRepository;
import com.hansarangdelivery.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;

@SpringBootTest(classes = HansarangDeliveryApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class AiResponseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AiResponseRepository aiResponseRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private RestTemplate restTemplate;

    protected User owner;
    protected User owner2;
    protected User manager;
    protected String ownerToken;
    protected String owner2Token;
    protected String managerToken;

    @BeforeEach
    void setUp() {
        aiResponseRepository.deleteAll();
        userRepository.deleteAll();

        owner = new User("owner1", passwordEncoder.encode("Password1!"), "owner1@example.com", UserRole.OWNER);
        userRepository.save(owner);
        ownerToken = jwtUtil.createToken(owner.getUsername(), owner.getRole(), owner.getId());

        owner2 = new User("owner2", passwordEncoder.encode("Password1!"), "owner2@example.com", UserRole.OWNER);
        userRepository.save(owner2);
        owner2Token = jwtUtil.createToken(owner2.getUsername(), owner2.getRole(), owner2.getId());

        manager = new User("manager", passwordEncoder.encode("Password1!"), "manager@example.com", UserRole.MANAGER);
        userRepository.save(manager);
        managerToken = jwtUtil.createToken(manager.getUsername(), manager.getRole(), manager.getId());
    }

    private AiApiResponseDto createMockApiResponse(String mockText) {
        AiApiResponseDto responseDto = new AiApiResponseDto();
        AiApiResponseDto.Candidate candidate = new AiApiResponseDto.Candidate();
        AiApiResponseDto.Content content = new AiApiResponseDto.Content();
        AiApiResponseDto.Part part = new AiApiResponseDto.Part();
        part.setText(mockText);
        content.setParts(List.of(part));
        candidate.setContent(content);
        responseDto.setCandidates(List.of(candidate));
        return responseDto;
    }

    @Test
    @DisplayName("AI 응답 생성 API 테스트")
    void testCreateAiResponse() throws Exception {
        // Given
        AiRequestDto requestDto = new AiRequestDto();
        requestDto.setRequest("test request");

        String mockResponseText = "mock response";
        AiApiResponseDto mockResponseDto = createMockApiResponse(mockResponseText);
        ResponseEntity<AiApiResponseDto> mockResponseEntity = new ResponseEntity<>(mockResponseDto, HttpStatus.OK);

        when(restTemplate.exchange( // 실제로 Ai studio 에서 데이터를 받아올 것이 아니기에 mock 처리
            ArgumentMatchers.anyString(),
            ArgumentMatchers.eq(HttpMethod.POST),
            ArgumentMatchers.<HttpEntity<?>>any(),
            ArgumentMatchers.eq(AiApiResponseDto.class)
        )).thenReturn(mockResponseEntity);

        // When & Then
        mockMvc.perform(post("/api/ai")
                .header(JwtUtil.AUTHORIZATION_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Ai 응답 생성 성공"))
            .andExpect(jsonPath("$.data").value(mockResponseText));

        List<AiResponse> responses = aiResponseRepository.findAll();
        assertFalse(responses.isEmpty());
        AiResponse response = responses.get(0);
        assertEquals(owner.getId(), response.getUserId());
        assertEquals("test request", response.getRequest());
        assertEquals(mockResponseText, response.getResponse());
    }

    @Test
    @DisplayName("자신이 생성한 AI 응답 단건 조회 테스트")
    void testReadAiResponse_Success() throws Exception {
        // Given
        AiResponse aiResponse = new AiResponse(owner.getId(), "test request", "test response");
        aiResponseRepository.save(aiResponse);

        // When & Then
        mockMvc.perform(get("/api/ai/" + aiResponse.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, ownerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("조회 성공"))
            .andExpect(jsonPath("$.data.request").value("test request"))
            .andExpect(jsonPath("$.data.response").value("test response"));
    }

    @Test
    @DisplayName("다른 유저가 생성한 AI 응답 단건 조회 테스트 (MANAGER 권한)")
    void testReadAiResponse_AsManager_Success() throws Exception {
        // Given
        AiResponse aiResponse = new AiResponse(owner.getId(), "test request", "test response");
        aiResponseRepository.save(aiResponse);

        // When & Then
        mockMvc.perform(get("/api/ai/" + aiResponse.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, managerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("조회 성공"))
            .andExpect(jsonPath("$.data.request").value("test request"))
            .andExpect(jsonPath("$.data.response").value("test response"));
    }

    @Test
    @DisplayName("다른 유저가 생성한 AI 응답 단건 조회 테스트 (OWNER 권한)")
    void testReadAiResponse_Fail() throws Exception {
        // Given
        AiResponse aiResponse = new AiResponse(owner.getId(), "test request", "test response");
        aiResponseRepository.save(aiResponse);

        // When & Then
        mockMvc.perform(get("/api/ai/" + aiResponse.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, owner2Token))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("AI 응답 전체 조회 테스트 (OWNER 권한)")
    void testSearchAiResponses() throws Exception {
        // Given
        AiResponse aiResponse1 = new AiResponse(owner.getId(), "test request 1", "test response 1");
        AiResponse aiResponse2 = new AiResponse(owner.getId(), "test request 2", "test response 2");
        aiResponseRepository.save(aiResponse1);
        aiResponseRepository.save(aiResponse2);

        // When & Then
        mockMvc.perform(get("/api/ai")
                .header(JwtUtil.AUTHORIZATION_HEADER, ownerToken)
                .param("page", "0")
                .param("size", "10")
                .param("isAsc", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("조회 성공"))
            .andExpect(jsonPath("$.data.content").isArray())
            .andExpect(jsonPath("$.data.content.length()").value(greaterThanOrEqualTo(2)));
    }

    @Test
    @DisplayName("자신이 생성한 AI 응답 삭제 API 테스트")
    void testDeleteAiResponseTest_Success() throws Exception {
        // Given
        AiResponse aiResponse = new AiResponse(owner.getId(), "test request", "test response");
        aiResponseRepository.save(aiResponse);

        // When & Then
        mockMvc.perform(delete("/api/ai/" + aiResponse.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, ownerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("삭제 성공"));

        assertFalse(aiResponseRepository.findById(aiResponse.getId()).isPresent());
    }

    @Test
    @DisplayName("다른 유저가 생성한 AI 응답 삭제 API 테스트 (MANAGER 권한)")
    void testDeleteAiResponseTest_AsManager_Success() throws Exception {
        // Given
        AiResponse aiResponse = new AiResponse(owner.getId(), "test request", "test response");
        aiResponseRepository.save(aiResponse);

        // When & Then
        mockMvc.perform(delete("/api/ai/" + aiResponse.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, managerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("삭제 성공"));

        assertFalse(aiResponseRepository.findById(aiResponse.getId()).isPresent());
    }

    @Test
    @DisplayName("다른 유저가 생성한 AI 응답 삭제 API 테스트 (OWNER 권한)")
    void testDeleteAiResponseTest_Fail() throws Exception {
        // Given
        AiResponse aiResponse = new AiResponse(owner.getId(), "test request", "test response");
        aiResponseRepository.save(aiResponse);

        // When & Then
        mockMvc.perform(delete("/api/ai/" + aiResponse.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, owner2Token))
            .andExpect(status().isForbidden());

        assertTrue(aiResponseRepository.findById(aiResponse.getId()).isPresent());
    }
}
