package com.hansarangdelivery.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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


    @Column(name = "menu_quantity", nullable = false)
    private int quantity;


    @Column(name = "menu_total_price", nullable = false)
    private int menuTotalPrice;


    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;


    public OrderItem(int quantity, int menuTotalPrice, Order order) {
        this.quantity = quantity;
        this.menuTotalPrice = menuTotalPrice;
        this.order = order;

    }

}