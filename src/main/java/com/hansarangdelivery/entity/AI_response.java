package com.hansarangdelivery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "p_AI_response")
@NoArgsConstructor
public class AI_response extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ai_response_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id; // 고유 식별자

    @Column(name = "request_text", columnDefinition = "TEXT", nullable = false)
    private String request_text;

    @Column(name = "response_text", columnDefinition = "TEXT")
    private String response_text;

}


