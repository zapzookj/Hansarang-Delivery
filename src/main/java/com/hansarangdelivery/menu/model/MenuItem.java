package com.hansarangdelivery.menu.model;

import com.hansarangdelivery.menu.dto.MenuItemUpdateDto;
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
@Entity
@Table(name = "p_menu_item")
@Where(clause = "deleted_at IS NULL")
public class MenuItem extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, insertable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID restaurantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    // 품절, 재고없음, 메뉴 수정중 등등 숨김 처리
    @Column(nullable = false)
    private Boolean isAvailable;

    public MenuItem(String name, Integer price, UUID restaurantId) {
        this.name = name;
        this.price = price;
        this.restaurantId = restaurantId;
        this.isAvailable = true;
    }

    public void update(MenuItemUpdateDto requestDto) {
        this.name = requestDto.getName() == null ? this.name : requestDto.getName();
        this.price = requestDto.getPrice() == null ? this.price : requestDto.getPrice();
    }

    public void setAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
