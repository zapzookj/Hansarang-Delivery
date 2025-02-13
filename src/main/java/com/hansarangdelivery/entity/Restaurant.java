package com.hansarangdelivery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
    @Column(name = "restaurant_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id; // 고유 식별자

    @Column(name = "restaurant_name",nullable = false, length = 100)
    private String name; // 가게 이름

    @OneToOne
    @JoinColumn(name="categories_id",nullable = false)
    private CategoryEntity category; // 카테고리

    @ManyToOne
    @JoinColumn(name="location_id",nullable = false)
    private Location location; // 위치

    @ManyToOne
    @JoinColumn(name="id",nullable = false)
    private User owner; // 소유자

    @Column(nullable = false)
    private Boolean status; // 가게 상태 (운영 중 여부)
}
