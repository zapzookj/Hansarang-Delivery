package com.hansarangdelivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "p_review")
public class Review extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID restaurantId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int rating;

    public Review(UUID orderId, UUID restaurantId, String content, int rating) {
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.content = content;
        this.rating = rating;
    }

    public void update(String content, int rating) {
        this.content = content;
        this.rating = rating;
    }
}
