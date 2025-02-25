package com.hansarangdelivery.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansarangdelivery.HansarangDeliveryApplication;
import com.hansarangdelivery.address.model.DeliveryAddress;
import com.hansarangdelivery.address.repository.DeliveryAddressRepository;
import com.hansarangdelivery.category.model.Category;
import com.hansarangdelivery.category.repository.CategoryRepository;
import com.hansarangdelivery.location.model.Location;
import com.hansarangdelivery.location.repository.LocationRepository;
import com.hansarangdelivery.menu.model.MenuItem;
import com.hansarangdelivery.menu.repository.MenuItemRepository;
import com.hansarangdelivery.order.model.Order;
import com.hansarangdelivery.order.model.OrderItem;
import com.hansarangdelivery.order.model.OrderStatus;
import com.hansarangdelivery.order.model.OrderType;
import com.hansarangdelivery.order.repository.OrderRepository;
import com.hansarangdelivery.restaurant.model.Restaurant;
import com.hansarangdelivery.restaurant.repository.RestaurantRepository;
import com.hansarangdelivery.review.dto.ReviewRequestDto;
import com.hansarangdelivery.review.model.Review;
import com.hansarangdelivery.review.repository.ReviewRepository;
import com.hansarangdelivery.security.jwt.JwtUtil;
import com.hansarangdelivery.user.model.User;
import com.hansarangdelivery.user.model.UserRole;
import com.hansarangdelivery.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = HansarangDeliveryApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ReviewApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    // DeliveryAddress 데이터를 위해 Repository 주입
    @Autowired
    private DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JwtUtil jwtUtil;

    private UUID testRestaurantId;
    private UUID testCategoryId;
    private UUID testLocationId;
    private UUID testMenuId;
    private UUID testOrderId;
    private UUID testReviewId;

    private UUID readOnlyOrderId;


    protected User customer;
    protected String customerToken;

    protected User owner;
    protected String ownerToken;

    protected Review createdReview;

    @BeforeEach
    @Transactional
    void setup() {
        // 기존 데이터 삭제
        orderRepository.deleteAll();
        userRepository.deleteAll();
        restaurantRepository.deleteAll();
        categoryRepository.deleteAll();
        locationRepository.deleteAll();
        menuItemRepository.deleteAll();
        deliveryAddressRepository.deleteAll();
        reviewRepository.deleteAll();

        // 1. 테스트용 사용자 생성(Owner)
        owner = new User("owner11", passwordEncoder.encode("Password1!"), "owner11@example.com", UserRole.OWNER);
        userRepository.save(owner);
        ownerToken = jwtUtil.createToken(owner.getUsername(), owner.getRole(), owner.getId());

        customer = new User("customer22", passwordEncoder.encode("Password1!"), "customer22@example.com", UserRole.CUSTOMER);
        userRepository.save(customer);
        customerToken = jwtUtil.createToken(customer.getUsername(), customer.getRole(), customer.getId());

        // 2. 테스트용 카테고리 생성
        Category testCategory = new Category("양식");
        categoryRepository.save(testCategory);
        testCategoryId = testCategory.getId();

        // 3. CSV 데이터 기반 테스트용 위치(Location) 생성
        // CSV 첫 번째 행의 값 사용: lawCode: 1114010300, city: 서울특별시, district: 중구, subDistrict: 태평로1가, roadNameCode: 111102005001
        Location testLocation = new Location("1114010300", "서울특별시", "중구", "태평로1가", "111102005001");
        locationRepository.save(testLocation);
        testLocationId = testLocation.getId();

        // 4. 기본 배송지 생성 (테스트용 사용자와 위 Location 사용)
        // DeliveryAddress 생성자: (User, UUID locationId, String requestMessage, boolean isDefault)
        DeliveryAddress defaultAddress = new DeliveryAddress(owner, testLocationId, "문 앞에 놓아주세요", true);
        deliveryAddressRepository.save(defaultAddress);

        // 5. 테스트용 레스토랑 생성 및 저장
        Restaurant testRestaurant = new Restaurant("한사랑 치킨", testCategoryId, owner.getId(), testLocationId);
        restaurantRepository.save(testRestaurant);
        testRestaurantId = testRestaurant.getId();

        // 레스토랑 저장 확인
        Optional<Restaurant> savedRestaurant = restaurantRepository.findById(testRestaurant.getId());
        if (savedRestaurant.isEmpty()) {
            throw new RuntimeException("테스트 데이터 저장 실패: restaurantId = " + testRestaurantId);
        }

        // 6. 테스트용 메뉴 생성 및 저장
        MenuItem testMenu = new MenuItem("후라이드 치킨", 20000, testRestaurantId);
        menuItemRepository.save(testMenu);
        testMenuId = testMenu.getId();

        // 7. 테스트용 주문 생성
        List<OrderItem> orderItems = new ArrayList<>();
        Order testOrder = new Order(
            customer.getId(),
            testRestaurantId,
            "한사랑 치킨",
            40000,
            OrderType.ONLINE,
            OrderStatus.PENDING,
            "111102005001",
            "서울특별시 강남구 삼성동",
            "문 앞에 놓아주세요",
            orderItems
        );

        // 7-1. 조회용 주문 생성
        List<OrderItem> readOnlyOrderItems = new ArrayList<>();
        Order readOnlyOrder = new Order(
            customer.getId(),
            testRestaurantId,
            "한사랑 치킨",
            50_000,
            OrderType.ONLINE,
            OrderStatus.PENDING,
            "111102005001",
            "서울특별시 강남구 삼성동",
            "문 앞에 놔둬주세요",
            orderItems
        );

        // OrderItem 연결
        OrderItem item = new OrderItem(testMenuId, "후라이드 치킨", 20000, 2, testOrder);
        orderItems.add(item);

        OrderItem readOnlyItem = new OrderItem(testMenuId, "후라이드 치킨", 20000, 2, testOrder);
        readOnlyOrderItems.add(readOnlyItem);

        testOrder.setOrderStatus(OrderStatus.COMPLETED);
        readOnlyOrder.setOrderStatus(OrderStatus.COMPLETED);

        orderRepository.save(testOrder);
        testOrderId = testOrder.getId();

        orderRepository.save(readOnlyOrder);
        readOnlyOrderId = readOnlyOrder.getId();

        Review readOnlyReview = new Review(readOnlyOrderId, testRestaurantId, "존맛 치킨이네요 짱", 4);
        readOnlyReview.setCreatedBy(customer.getId().toString());
        createdReview = reviewRepository.save(readOnlyReview);
        testReviewId = createdReview.getId();

        // DB 반영 후 영속성 컨텍스트 초기화 (버전 충돌 방지)
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 주문 생성 API 테스트
     */
    @Test
    @DisplayName("리뷰 생성 API 테스트")
    public void createOrder() throws Exception {

        ReviewRequestDto requestDto = new ReviewRequestDto();
        requestDto.setOrderId(testOrderId);
        requestDto.setRestaurantId(testRestaurantId);
        requestDto.setContent("존맛탱이에요 굿굿");
        requestDto.setRating(5);

        // 주문 생성 API 호출 후 결과 검증

        mockMvc.perform(post("/api/reviews")
                .header(JwtUtil.AUTHORIZATION_HEADER, customerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("리뷰 작성 완료"))
            // 응답 DTO에 있는 필드 검증
            .andExpect(jsonPath("$.data.orderId").value(testOrderId.toString()))
            .andExpect(jsonPath("$.data.content").value("존맛탱이에요 굿굿"))
            .andExpect(jsonPath("$.data.rating").value(5));
    }

    @Test
    @DisplayName("리뷰 단건 조회")
    void readOrder() throws Exception {

        mockMvc.perform(get("/api/reviews/" + createdReview.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, customerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("리뷰 조회 완료"))
            // 응답 DTO에 있는 필드 검증
            .andExpect(jsonPath("$.data.orderId").value(readOnlyOrderId.toString()))
            .andExpect(jsonPath("$.data.content").value("존맛 치킨이네요 짱"))
            .andExpect(jsonPath("$.data.rating").value(4));
    }

    @Test
    @DisplayName("내 리뷰 조회")
    void searchMyReview() throws Exception {

        mockMvc.perform(get("/api/reviews/me")
                .header(JwtUtil.AUTHORIZATION_HEADER, customerToken)
                .param("page", "0")
                .param("size", "10")
                .param("isAsc", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.content").isArray())
            .andExpect(jsonPath("$.data.content.length()").value(greaterThanOrEqualTo(1)));
    }

    @Test
    @DisplayName("식당 리뷰 조회")
    void searchRestaurantReview() throws Exception {

        mockMvc.perform(get("/api/reviews")
                .header(JwtUtil.AUTHORIZATION_HEADER, customerToken)
                .param("restaurantId", testRestaurantId.toString())
                .param("page", "0")
                .param("size", "10")
                .param("isAsc", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.content").isArray())
            .andExpect(jsonPath("$.data.content.length()").value(greaterThanOrEqualTo(1)));
    }

    @Test
    @DisplayName("리뷰 수정")
    void updateReview() throws Exception {

        ReviewRequestDto requestDto = new ReviewRequestDto(
            "리뷰 수정했어요", 3
        );

        mockMvc.perform(put("/api/reviews/" + testReviewId)
                .header(JwtUtil.AUTHORIZATION_HEADER, customerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("리뷰 수정 완료"))
            // 응답 DTO 검증
            .andExpect(jsonPath("$.data.createdBy").value(customer.getId().toString()))
            .andExpect(jsonPath("$.data.content").value(requestDto.getContent()))
            .andExpect(jsonPath("$.data.rating").value(requestDto.getRating()));

    }

    @Test
    @DisplayName("리뷰 삭제")
    void deleteReview() throws Exception {

        mockMvc.perform(delete("/api/reviews/" + testReviewId)
                .header(JwtUtil.AUTHORIZATION_HEADER, customerToken)
                .content(objectMapper.writeValueAsString(createdReview)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("리뷰 삭제 완료"))
            .andExpect(jsonPath("$.data.createdBy").value(customer.getId().toString()));
    }
}




