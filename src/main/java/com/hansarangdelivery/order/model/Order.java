package com.hansarangdelivery.order.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hansarangdelivery.global.model.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;

    @Column(name = "store_name", length = 100, nullable = false)
    private String storeName;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;

//    @Column(name = "delivery_address", length = 100, nullable = false)
//    private UUID deliveryAddress; // 변경된 필드

    @Column(name = "road_name_code", length = 100, nullable = false)
    private String roadNameCode; // 변경된 필드

    @Column(name = "detail_address", length = 100, nullable = false)
    private String detailAddress;

    @Column(name = "delivery_request", length = 100, nullable = false)
    private String deliveryRequest;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();

    // 변경된 생성자
    public Order(Long userId, UUID restaurantId, String storeName, int totalPrice, OrderType orderType, OrderStatus status,
                 String roadNameCode, String detailAddress, String deliveryRequest, List<OrderItem> orderItems) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.storeName = storeName;
        this.totalPrice = totalPrice;
        this.orderType = orderType;
        this.orderStatus = status;
        this.roadNameCode =roadNameCode;
        this.detailAddress = detailAddress;
        this.deliveryRequest = deliveryRequest;
        this.orderItems = (orderItems != null) ? orderItems : new ArrayList<>();
        this.orderItems.forEach(item -> item.setOrder(this));
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems.clear();
        this.orderItems.addAll(orderItems);
    }
}
