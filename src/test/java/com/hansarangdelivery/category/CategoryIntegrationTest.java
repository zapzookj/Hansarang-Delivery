package com.hansarangdelivery.category;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansarangdelivery.HansarangDeliveryApplication;
import com.hansarangdelivery.category.dto.CategoryRequestDto;
import com.hansarangdelivery.category.model.Category;
import com.hansarangdelivery.user.model.User;
import com.hansarangdelivery.user.model.UserRole;
import com.hansarangdelivery.security.jwt.JwtUtil;
import com.hansarangdelivery.category.repository.CategoryRepository;
import com.hansarangdelivery.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = HansarangDeliveryApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@Transactional
public class CategoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    private String url = "/api/categories";

    @Test
    @DisplayName("category register API test")
    public void createCategory() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("새로운 카테고리");

        String jwtToken = getJwtTokenByManager();

        mockMvc.perform(post(url)
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("카테고리 생성 성공"));
    }

    @Test
    @DisplayName("category update API test")
    public void updateCategory() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("업데이트된 카테고리");
        UUID categoryId = createTestCategory();
        String jwtToken = getJwtTokenByManager();

        mockMvc.perform(put(url + "/"+categoryId)
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("카테고리 수정 성공"))
            .andExpect(jsonPath("$.data.name").value("업데이트된 카테고리"));
    }

    @Test
    @DisplayName("category delete API test")
    public void deleteCategory() throws Exception {
        UUID categoryId = createTestCategory();
        String jwtToken = getJwtTokenByManager();
        mockMvc.perform(delete(url +"/" +categoryId)
                .header("Authorization", jwtToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("카테고리 삭제 성공"));
    }

    @Test
    @DisplayName("category search API test")
    public void searchCategory() throws Exception {
        String jwtToken = getJwtTokenByManager();
        mockMvc.perform(get(url)
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("카테고리 검색 성공"));
    }

    private String getJwtTokenByManager() {
        String jwtToken;
        User user = new User("manager1", "1111", "manager1@mail.com", UserRole.MANAGER);
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        jwtToken = jwtUtil.createToken("manager1", UserRole.MANAGER, user.getId());
        return jwtToken;
    }

    private UUID createTestCategory() {
        Category category = new Category("테스트 카테고리");
        categoryRepository.save(category);
        entityManager.flush();
        entityManager.clear();
        return category.getId();
    }
}
