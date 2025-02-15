package com.hansarangdelivery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "p_restaurant")
@NoArgsConstructor
public class Restaurant extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id; // 고유 식별자

    @Column(name = "name",nullable = false, length = 255)
    private String name; // 가게 이름

    @Column(name="category_id",nullable = false)
    private UUID category; // 카테고리


    @Column(name="location",nullable = false)
    private Long location; // 위치코드


    @JoinColumn(name="owner_id",nullable = false)
    private UUID owner; // 소유자

    @Column(name = "status", nullable = false)
    private Boolean status; // 가게 상태 (운영 중 여부)
}
