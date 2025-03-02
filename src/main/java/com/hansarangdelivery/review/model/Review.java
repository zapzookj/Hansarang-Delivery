package com.hansarangdelivery.review.model;

import com.hansarangdelivery.global.model.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "p_review")
@Where(clause = "deleted_at IS NULL")
public class Review extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false)
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
