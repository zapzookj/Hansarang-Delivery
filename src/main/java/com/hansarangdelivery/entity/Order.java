package com.hansarangdelivery.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "store_name" ,length = 100, nullable = false)
    private String storeName;

    @Column(name ="total_price",nullable = false)
    private int price;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "delivery_address", length = 100, nullable = false)
    private String deliveryAddress;

    @Column(name = "delivery_request", length = 100, nullable = false)
    private String deliveryRequest;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    public Order(String storeName, int price, OrderStatus status, String deliveryAddress, String deliveryRequest) {
        this.storeName = storeName;
        this.price = price;
        this.status = status;
        this.deliveryAddress = deliveryAddress;
        this.deliveryRequest = deliveryRequest;
        this.orderItems = new ArrayList<>(); // 생성자에서 초기화
    }


}




