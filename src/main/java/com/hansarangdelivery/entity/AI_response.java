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
@Table(name = "p_ai_response")
@NoArgsConstructor
public class AI_response extends TimeStamped{
    // 명확한 작동 방식을 생각하며 넣은게 아니라서 다 갈아엎어도 됩니다.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id; // 고유 식별자

    @Column(name = "request_text", columnDefinition = "TEXT", nullable = false)
    private String request_text; // 요청 텍스트?

    @Column(name = "response_text", columnDefinition = "TEXT")
    private String response_text; // AI의 답변 텍스트?

}


