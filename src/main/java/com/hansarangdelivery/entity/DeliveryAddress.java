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
@Table(name = "p_delivery_address")
public class DeliveryAddress extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "location_id", nullable = false)
    private UUID locationId;  // Location의 ID만 저장

    @Column(name = "request_message")
    private String requestMessage;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    public DeliveryAddress(User user, UUID locationId, String requestMessage) {
        this.user = user;
        this.locationId = locationId;
        this.requestMessage = requestMessage;
        this.isDefault = true;
    }
}

