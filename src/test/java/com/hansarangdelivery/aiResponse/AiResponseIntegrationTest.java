package com.hansarangdelivery.aiResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansarangdelivery.HansarangDeliveryApplication;
import com.hansarangdelivery.dto.AiApiResponseDto;
import com.hansarangdelivery.dto.AiRequestDto;
import com.hansarangdelivery.dto.AiResponseDto;
import com.hansarangdelivery.entity.AiResponse;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.entity.UserRole;
import com.hansarangdelivery.jwt.JwtUtil;
import com.hansarangdelivery.repository.AiResponseRepository;
import com.hansarangdelivery.repository.UserRepository;
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
    protected User manager;
    protected String ownerToken;
    protected String managerToken;

    @BeforeEach
    void setUp() {
        aiResponseRepository.deleteAll();
        userRepository.deleteAll();

        owner = new User("owner", passwordEncoder.encode("Password1!"), "owner@example.com", UserRole.OWNER);
        userRepository.save(owner);
        ownerToken = jwtUtil.createToken(owner.getUsername(), owner.getRole());

        manager = new User("manager", passwordEncoder.encode("Password1!"), "manager@example.com", UserRole.MANAGER);
        userRepository.save(manager);
        managerToken = jwtUtil.createToken(manager.getUsername(), manager.getRole());
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


}
