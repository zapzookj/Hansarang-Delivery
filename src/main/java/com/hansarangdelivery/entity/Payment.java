package com.hansarangdelivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod; // 결제 방법 (카드, 간편결제 등)

    @Column(nullable = false)
    private boolean isSuccess; // 결제 성공 여부

    @Column(nullable = false)
    private int totalPrice; // 결제 금액

    @Column(unique = true)
    private String transactionId; // 결제 시스템의 거래 ID (Mock 데이터)
}
