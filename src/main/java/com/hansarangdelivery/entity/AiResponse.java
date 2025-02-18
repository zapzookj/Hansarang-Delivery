package com.hansarangdelivery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "p_ai_response")
@NoArgsConstructor
@AllArgsConstructor
public class AiResponse extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id; // 고유 식별자

    @Column(nullable = false)
    private Long userId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String request;

    @Column(columnDefinition = "TEXT")
    private String response;

    public AiResponse(Long userId, String request, String response) {
        this.userId = userId;
        this.request = request;
        this.response = response;
    }
}


