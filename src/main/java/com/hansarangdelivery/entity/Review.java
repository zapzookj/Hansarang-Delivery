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
    private UUID order_id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int rating;

    public Review(UUID orderId, String content, int rating) {
        this.order_id = orderId;
        this.content = content;
        this.rating = rating;
    }
}
