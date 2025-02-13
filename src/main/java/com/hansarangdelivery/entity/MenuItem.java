package com.hansarangdelivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class MenuItem extends TimeStamped{

    @Id
    @Column(updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;
}
