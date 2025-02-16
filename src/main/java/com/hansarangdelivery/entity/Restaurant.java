package com.hansarangdelivery.entity;

import com.hansarangdelivery.dto.RestaurantRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
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

    @Column(name="owner_id",nullable = false)
    private UUID owner; // 소유자

    @Column(name="location_id",nullable = false)
    private UUID location; // 위치


    @Column(name = "status", nullable = false)
    private boolean status; // 가게 상태 (운영 중 여부)

    public Restaurant(String name, UUID categoryId, UUID ownerId, UUID locationId) {
        super();
        this.name = name;
        this.category = categoryId;
        this.owner = ownerId; //
        this.location = locationId; // locationId로 초기화
        this.status = false; // 초기 상태를 '닫음'으로 설정
    }

    public boolean getStatus(){
        return this.status;
    }

    public void update(String name, UUID categoryId, UUID ownerId, UUID locationId) {
        this.name = name;
        this.category = categoryId;
        this.owner = ownerId;
        this.location = locationId;
    }
}
