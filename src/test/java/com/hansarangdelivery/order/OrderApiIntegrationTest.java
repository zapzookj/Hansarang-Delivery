package com.hansarangdelivery.order;

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
import com.hansarangdelivery.order.dto.OrderItemRequestDto;
import com.hansarangdelivery.order.dto.OrderRequestDto;
import com.hansarangdelivery.security.jwt.JwtUtil;
import com.hansarangdelivery.order.model.Order;
import com.hansarangdelivery.order.model.OrderItem;
import com.hansarangdelivery.order.model.OrderStatus;
import com.hansarangdelivery.order.model.OrderType;
import com.hansarangdelivery.order.repository.OrderRepository;
import com.hansarangdelivery.restaurant.model.Restaurant;
import com.hansarangdelivery.restaurant.repository.RestaurantRepository;
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
public class OrderApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JwtUtil jwtUtil;


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

    private UUID testRestaurantId;
    private UUID testCategoryId;
    private UUID testLocationId;
    private UUID testMenuId;

    private Order savedOrder;

    protected User owner;
    protected String ownerToken;

    protected User manager;

    protected String managerToken;





    @BeforeEach
    void setup() {
        // 기존 데이터 삭제
        orderRepository.deleteAll();
        userRepository.deleteAll();
        restaurantRepository.deleteAll();
        categoryRepository.deleteAll();
        locationRepository.deleteAll();
        menuItemRepository.deleteAll();
        deliveryAddressRepository.deleteAll();

        // 1. 테스트용 사용자 생성(Owner)
        owner = new User("user", passwordEncoder.encode("Password1!"), "User@example.com", UserRole.OWNER);
        userRepository.save(owner);
        ownerToken = jwtUtil.createToken(owner.getUsername(), owner.getRole(), owner.getId());

        manager = new User("manager", passwordEncoder.encode("Password1!"), "manager@example.com", UserRole.MANAGER);
        userRepository.save(manager);
        managerToken = jwtUtil.createToken(manager.getUsername(), manager.getRole(), owner.getId());

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
            owner.getId(),
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
        // OrderItem 연결
        OrderItem item = new OrderItem(testMenuId, "후라이드 치킨", 20000, 2, testOrder);
        orderItems.add(item);

        savedOrder = orderRepository.save(testOrder);

        // DB 반영 후 영속성 컨텍스트 초기화 (버전 충돌 방지)
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 주문 생성 API 테스트
     */
    @Test
    @DisplayName("주문 생성 API 테스트")
    public void createOrder() throws Exception {
        UUID validRestaurantId = testRestaurantId;

        List<OrderItemRequestDto> orderItemDtos = List.of(
            new OrderItemRequestDto(testMenuId, 2)
        );

        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setUserId(owner.getId());
        requestDto.setRestaurantId(validRestaurantId);
        requestDto.setRoadName("111102005001");
        requestDto.setDetailAddress("서울특별시 강남구 삼성동");
        requestDto.setDeliveryRequest("문 앞에 놓아주세요");
        requestDto.setOrderType("ONLINE");
        requestDto.setOrderStatus("PENDING");
        requestDto.setMenu(orderItemDtos);

        // 주문 생성 API 호출 후 결과 검증

        mockMvc.perform(post("/api/orders")
                .header(JwtUtil.AUTHORIZATION_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("주문 생성 성공"))
            // 응답 DTO에 있는 필드 검증
            .andExpect(jsonPath("$.data.restaurantId").value(validRestaurantId.toString()))
            .andExpect(jsonPath("$.data.status").value(requestDto.getOrderStatus()))
            .andExpect(jsonPath("$.data.roadNameCode").value("111102005001"))
            .andExpect(jsonPath("$.data.detailAddress").value("서울특별시 강남구 삼성동"))
            .andExpect(jsonPath("$.data.deliveryRequest").value("문 앞에 놓아주세요"))
            // orderItems 배열 검증
            .andExpect(jsonPath("$.data.orderItems.length()").value(1))
            .andExpect(jsonPath("$.data.orderItems[0].menuId").value(testMenuId.toString()))
            .andExpect(jsonPath("$.data.orderItems[0].menuName").value("후라이드 치킨"))
            .andExpect(jsonPath("$.data.orderItems[0].menuPrice").value(20000))
            .andExpect(jsonPath("$.data.orderItems[0].quantity").value(2))
            .andExpect(jsonPath("$.data.orderItems[0].menuTotalPrice").value(40000));
    }

    @Test
    @DisplayName("특정 주문 정보 조회 API 테스트")
    void readOrder() throws Exception {
        UUID validRestaurantId = testRestaurantId;

        mockMvc.perform(get("/api/orders/" + savedOrder.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, managerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("특정 주문 상세 정보 조회 성공"))
            // 응답 DTO에 있는 필드 검증
            .andExpect(jsonPath("$.data.restaurantId").value(validRestaurantId.toString()))
            .andExpect(jsonPath("$.data.status").value("PENDING"))
            .andExpect(jsonPath("$.data.roadNameCode").value("111102005001"))
            .andExpect(jsonPath("$.data.detailAddress").value("서울특별시 강남구 삼성동"))
            .andExpect(jsonPath("$.data.deliveryRequest").value("문 앞에 놓아주세요"))
            // orderItems 배열 검증
            .andExpect(jsonPath("$.data.orderItems.length()").value(1))
            .andExpect(jsonPath("$.data.orderItems[0].menuId").value(testMenuId.toString()))
            .andExpect(jsonPath("$.data.orderItems[0].menuName").value("후라이드 치킨"))
            .andExpect(jsonPath("$.data.orderItems[0].menuPrice").value(20000))
            .andExpect(jsonPath("$.data.orderItems[0].quantity").value(2))
            .andExpect(jsonPath("$.data.orderItems[0].menuTotalPrice").value(40000));
    }


    @Test
    @DisplayName("내 주문 정보 조회 API 테스트")
    void readMyOrder() throws Exception {
        UUID validRestaurantId = testRestaurantId;

        mockMvc.perform(get("/api/orders/me/" + savedOrder.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, ownerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("내 주문 상세 정보 조회 성공"))
            // 응답 DTO에 있는 필드 검증
            .andExpect(jsonPath("$.data.restaurantId").value(validRestaurantId.toString()))
            .andExpect(jsonPath("$.data.status").value("PENDING"))
            .andExpect(jsonPath("$.data.roadNameCode").value("111102005001"))
            .andExpect(jsonPath("$.data.detailAddress").value("서울특별시 강남구 삼성동"))
            .andExpect(jsonPath("$.data.deliveryRequest").value("문 앞에 놓아주세요"))
            // orderItems 배열 검증
            .andExpect(jsonPath("$.data.orderItems.length()").value(1))
            .andExpect(jsonPath("$.data.orderItems[0].menuId").value(testMenuId.toString()))
            .andExpect(jsonPath("$.data.orderItems[0].menuName").value("후라이드 치킨"))
            .andExpect(jsonPath("$.data.orderItems[0].menuPrice").value(20000))
            .andExpect(jsonPath("$.data.orderItems[0].quantity").value(2))
            .andExpect(jsonPath("$.data.orderItems[0].menuTotalPrice").value(40000));
    }

    @Test
    @DisplayName("특정 주문 수정 API 테스트")
    void updateOrder() throws Exception {
        UUID validRestaurantId = testRestaurantId;

        List<OrderItemRequestDto> orderItemDtos = List.of(
            new OrderItemRequestDto(testMenuId, 5)
        );

        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setRoadName("222222222222");
        // 상세주소 변경 (예: 서울에서 부산)
        requestDto.setDetailAddress("부산광역시 해운대구 우동");
        // 배달 요청 메시지 변경
        requestDto.setDeliveryRequest("문 앞에 두고 가세요");
        // 주문 타입 변경: ONLINE → OFFLINE
        requestDto.setOrderType("OFFLINE");
        // 주문 상태 변경: PENDING → COMPLETED
        requestDto.setOrderStatus("COMPLETED");
        requestDto.setMenu(orderItemDtos);

        mockMvc.perform(put("/api/orders/" + savedOrder.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("주문 수정 성공"))
            // 응답 DTO 검증
            .andExpect(jsonPath("$.data.restaurantId").value(validRestaurantId.toString()))
            .andExpect(jsonPath("$.data.status").value("COMPLETED"))
            .andExpect(jsonPath("$.data.roadNameCode").value("222222222222"))
            .andExpect(jsonPath("$.data.detailAddress").value("부산광역시 해운대구 우동"))
            .andExpect(jsonPath("$.data.deliveryRequest").value("문 앞에 두고 가세요"))
            // 주문 항목 검증
            .andExpect(jsonPath("$.data.orderItems.length()").value(1))
            .andExpect(jsonPath("$.data.orderItems[0].menuId").value(testMenuId.toString()))
            .andExpect(jsonPath("$.data.orderItems[0].menuName").value("후라이드 치킨"))
            .andExpect(jsonPath("$.data.orderItems[0].menuPrice").value(20000))
            .andExpect(jsonPath("$.data.orderItems[0].quantity").value(5))
            .andExpect(jsonPath("$.data.orderItems[0].menuTotalPrice").value(100000));
    }

    @Test
    @DisplayName("주문 삭제 성공 API 테스트")
    void deleteOrder() throws Exception{

        mockMvc.perform(delete("/api/orders/"+savedOrder.getId())
            .header(JwtUtil.AUTHORIZATION_HEADER, ownerToken)
            .content(objectMapper.writeValueAsString(savedOrder)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("주문이 취소되었습니다."))
            .andExpect(jsonPath("$.data.status").value("CANCELED"));
    }


    @Test
    @DisplayName("주문 취소 불가능한 경우 (생성 후 5분 초과) API 테스트")
    void deleteOrderFail() throws Exception {

        entityManager.createNativeQuery("UPDATE p_order SET created_at = ? WHERE order_id = ?")
            .setParameter(1, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now().minusMinutes(6)))
            .setParameter(2, savedOrder.getId())
            .executeUpdate();

        entityManager.flush();
        entityManager.clear();


        // DELETE API 호출 (HTTP 상태 코드는 200, body의 statusCode가 400이어야 함)
        mockMvc.perform(delete("/api/orders/" + savedOrder.getId())
                .header(JwtUtil.AUTHORIZATION_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("주문 취소 불가능합니다."))
            .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    @DisplayName("주문 내역 조회 성공 API 테스트")
    void searchOrder() throws Exception{

        mockMvc.perform(get("/api/orders")
                .header(JwtUtil.AUTHORIZATION_HEADER, ownerToken)
                .param("orderId", savedOrder.getId().toString())
                .param("page", "0")
                .param("size", "10")
                .param("isAsc", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.content").isArray())
            .andExpect(jsonPath("$.data.content.length()").value(greaterThanOrEqualTo(1)));

    }





}




