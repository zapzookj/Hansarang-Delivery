package com.hansarangdelivery.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_item_id", columnDefinition = "uuid")
    private UUID id;


    @Column(name = "menu_quantity", nullable = false)
    private int quantity;


    @Column(name ="menu_price",nullable = false)
    private int price;


    @ManyToOne
    @JoinColumn(name="order_id",nullable = false)
    private Order order;



    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuItem menuItem;


    public OrderItem(int quantity, int price, Order order, MenuItem menuItem) {
        this.quantity = quantity;
        this.price = price;
        this.order = order;
        this.menuItem = menuItem;
    }

}