package com.hansarangdelivery.entity;

import com.hansarangdelivery.dto.MenuItemRequestDto;
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
@Entity
@Table(name = "p_menu_item")
public class MenuItem extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID restaurantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private boolean available;

    public MenuItem(String name, Integer price, UUID restaurantId) {
        this.name = name;
        this.price = price;
        this.restaurantId = restaurantId;
    }

    public void update(MenuItemRequestDto requestDto) {
        this.name = requestDto.getName() == null ? this.name : requestDto.getName();
        this.price = requestDto.getPrice() == null ? this.price : requestDto.getPrice();
        this.available = requestDto.getIsAvailable() == null ? this.available : requestDto.getIsAvailable();
    }
}
