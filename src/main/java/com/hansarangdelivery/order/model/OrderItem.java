package com.hansarangdelivery.order.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hansarangdelivery.global.model.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_item_id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "menu_id", nullable = false)
    private UUID menuId;  // 메뉴 ID 저장

    @Column(name = "menu_name", nullable = false)
    private String menuName;  // 메뉴 이름 저장

    @Column(name = "menu_price", nullable = false)
    private int menuPrice;  // 메뉴 단가 저장

    @Column(name = "menu_quantity", nullable = false)
    private int quantity;  // 주문 수량

    @Column(name = "menu_total_price", nullable = false)
    private int menuTotalPrice;  // 해당 메뉴의 총 가격 (menuPrice * quantity)

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    public OrderItem(UUID menuId, String menuName, int menuPrice, int quantity, Order order) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
        this.menuTotalPrice = menuPrice * quantity;
        this.order = order;
    }
}
