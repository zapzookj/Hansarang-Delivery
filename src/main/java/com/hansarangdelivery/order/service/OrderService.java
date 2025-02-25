package com.hansarangdelivery.order.service;

import com.hansarangdelivery.menu.model.MenuItem;
import com.hansarangdelivery.order.dto.OrderRequestDto;
import com.hansarangdelivery.order.dto.OrderResponseDto;
import com.hansarangdelivery.global.exception.ForbiddenActionException;
import com.hansarangdelivery.global.exception.ResourceNotFoundException;
import com.hansarangdelivery.order.model.Order;
import com.hansarangdelivery.order.model.OrderItem;
import com.hansarangdelivery.order.model.OrderStatus;
import com.hansarangdelivery.order.model.OrderType;
import com.hansarangdelivery.order.repository.OrderRepository;
import com.hansarangdelivery.order.repository.OrderRepositoryQueryImpl;
import com.hansarangdelivery.restaurant.model.Restaurant;
import com.hansarangdelivery.restaurant.service.RestaurantService;
import com.hansarangdelivery.address.service.DeliveryAddressService;
import com.hansarangdelivery.location.service.LocationService;
import com.hansarangdelivery.menu.service.MenuItemService;
import com.hansarangdelivery.payment.service.PaymentService;
import com.hansarangdelivery.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemService menuItemService;
    private final RestaurantService restaurantService; //  가게 정보 조회
    private final OrderRepositoryQueryImpl orderRepositoryQuery;
    private final PaymentService paymentService;
    private final DeliveryAddressService deliveryAddress;
    private final LocationService locationService;
    


    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto, User user) {
        Restaurant restaurant = restaurantService.getRestaurantById(requestDto.getRestaurantId());
        String storeName = restaurant.getName();

        UUID deliveryAddressId = deliveryAddress.readDeliveryAddress(user.getId()).getLocationId();
        String roadNameCode = locationService.readRoadName(deliveryAddressId).getRoadNameCode();


        List<OrderItem> orderItems = requestDto.getMenu().stream()
            .map(orderItemDto -> {
                MenuItem menuItem = menuItemService.getMenuById(orderItemDto.getMenuId());
                return new OrderItem(
                    menuItem.getId(),
                    menuItem.getName(),
                    menuItem.getPrice(),
                    orderItemDto.getQuantity(),
                    null
                );
            })
            .collect(Collectors.toList());


        // 주문 총 가격 계산
        int totalPrice = orderItems.stream()
            .mapToInt(OrderItem::getMenuTotalPrice)
            .sum();

        // 주문 객체 생성
        Order order = new Order(
            user.getId(),
            requestDto.getRestaurantId(),
            storeName,
            totalPrice,
            OrderType.valueOf(requestDto.getOrderType()),
            OrderStatus.PENDING,
            roadNameCode,
            requestDto.getDetailAddress(),
            requestDto.getDeliveryRequest(),
            orderItems
        );

        orderItems.forEach(item -> item.setOrder(order));

        orderRepository.save(order);

        return new OrderResponseDto(order);
    }


    public OrderResponseDto readOrder(UUID orderId) {
        Order order = findOrder(orderId);
        return new OrderResponseDto(order);
    }


    public OrderResponseDto readOrderByUser(UUID orderId, User user) {
        Order order = findOrder(orderId);

        // 주문이 현재 사용자에 속하는지 확인합니다.
        if (!order.getUserId().equals(user.getId())) {
            throw new ResourceNotFoundException("해당 주문은 사용자의 주문이 아닙니다.");
        }
        return new OrderResponseDto(order);
    }


    @Transactional
    public OrderResponseDto updateOrder(UUID orderId, OrderRequestDto requestDto) {
        // 1. 기존 주문 찾기
        Order order = findOrder(orderId);

        boolean isPaymentSuccess = paymentService.createMockPayment(orderId, order.getTotalPrice());

        if (!isPaymentSuccess) {
            throw new ForbiddenActionException("결제 실패");
        }

        // 2. 기본 정보 업데이트
        order.setDetailAddress(requestDto.getDetailAddress());
        order.setDeliveryRequest(requestDto.getDeliveryRequest());
        order.setOrderType(OrderType.valueOf(requestDto.getOrderType()));
        order.setOrderStatus(OrderStatus.valueOf(requestDto.getOrderStatus()));


        // 3. 새로운 OrderItem 추가
        List<OrderItem> newOrderItems = getOrderItems(requestDto, order);
        order.setOrderItems(newOrderItems);


        // 5. 총 주문 금액 업데이트 후 저장
        int totalPrice = newOrderItems.stream().mapToInt(OrderItem::getMenuPrice).sum();
        order.setTotalPrice(totalPrice);


        return new OrderResponseDto(order);

    }


    @Transactional
    public OrderResponseDto deleteOrder(UUID orderId, User user) {
        Order order = findOrder(orderId);

        // 주문이 생성된 시각
        LocalDateTime createdAt = order.getCreatedAt();

        // 현재 시각
        LocalDateTime now = LocalDateTime.now();

        // 주문이 5분이 초과되었는지 검사
        if (createdAt.plusMinutes(5).isBefore(now)) {
            throw new ForbiddenActionException("주문 등록 후 5분이 초과되어 주문 삭제 실패");
        }

        // 주문 취소 처리
        order.delete(now, user.getId().toString());

        // 주문 상태 변경 (예: CANCELED)
        order.setOrderStatus(OrderStatus.CANCELED);


        // 변경 사항 저장
        Order newOrder = orderRepository.save(order);
        return new OrderResponseDto(newOrder);
    }


    public Page<OrderResponseDto> searchOrders(UUID orderId, Pageable pageable) {

        Page<Order> orders = orderRepositoryQuery.searchByOrderId(orderId, pageable);

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("주문한 내역이 없습니다.");
        }
        return orders.map(OrderResponseDto::new);

    }


    private Order findOrder(UUID orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 주문입니다."));
    }


    private List<OrderItem> getOrderItems(OrderRequestDto requestDto, Order order) {
        return requestDto.getMenu().stream() //requestDto.getMenu()에서 OrderItemRequestDto 리스트를 가져옴.
            .map(itemDto -> {
                MenuItem menu = menuItemService.getMenuById(itemDto.getMenuId());
                return new OrderItem(
                    menu.getId(),
                    menu.getName(),
                    menu.getPrice(),
                    itemDto.getQuantity(),
                    order
                );
            })
            .collect(Collectors.toList());

    }



}


