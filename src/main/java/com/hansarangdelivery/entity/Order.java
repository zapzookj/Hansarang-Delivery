package com.hansarangdelivery.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Column(name = "store_name", length = 100, nullable = false)
    private String storeName;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;


    @Column(name = "delivery_address", length = 100, nullable = false)
    private String deliveryAddress;

    @Column(name = "delivery_request", length = 100, nullable = false)
    private String deliveryRequest;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order(String storeName, int totalPrice,OrderType orderType, OrderStatus status, String deliveryAddress, String deliveryRequest, List<OrderItem> orderItems) {
        this.storeName = storeName;
        this.totalPrice = totalPrice;
        this.orderType = orderType;
        this.status = status;
        this.deliveryAddress = deliveryAddress;
        this.deliveryRequest = deliveryRequest;
        this.orderItems = (orderItems != null) ? orderItems : new ArrayList<>(); // 생성자에서 초기화
        this.orderItems.forEach(item -> item.setOrder(this));
    }


}




