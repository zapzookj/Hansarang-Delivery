package com.hansarangdelivery.restaurant.model;

import com.hansarangdelivery.global.model.TimeStamped;
import com.hansarangdelivery.restaurant.dto.RestaurantRequestDto;
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
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@Table(name = "p_restaurant")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
public class Restaurant extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id; // 고유 식별자

    @Column(name = "name",nullable = false, length = 255)
    private String name; // 가게 이름

    @Column(name="category_id",nullable = false)
    private UUID category; // 카테고리

    @Column(name="owner_id",nullable = false)
    private Long owner; // 소유자

    @Column(name="location_id",nullable = false)
    private UUID location; // 위치

    @Column(name = "status", nullable = false)
    private boolean status; // 가게 상태 (운영 중 여부)

    public Restaurant(String name, UUID categoryId, Long ownerId, UUID locationId) {
//      테스트 코드 위해 남겨놓음
        this.name = name;
        this.category = categoryId;
        this.owner = ownerId; //
        this.location = locationId; // locationId로 초기화
        this.status = false; // 초기 상태를 '닫음'으로 설정
    }

    public Restaurant(RestaurantRequestDto requestDto){
        this.name = requestDto.getName();
        this.category = requestDto.getCategory_id();
        this.owner = requestDto.getOwner_id();
        this.location = requestDto.getLocation_id();
        this.status = false;
    }

    public boolean getStatus(){
        return this.status;
    }

    public void update(RestaurantRequestDto requestDto){
        if(requestDto.getName() != null){
            this.name = requestDto.getName();
        }
        if(requestDto.getLocation_id() != null){
            this.location = requestDto.getLocation_id();
        }
        if(requestDto.getCategory_id() != null){
            this.category = requestDto.getCategory_id();
        }
        if(requestDto.getOwner_id() != null){
            this.owner = requestDto.getOwner_id();
        }
        if(requestDto.isOpen()){
            this.status = true;
        }else{
            this.status = false;
        }
    }
}
