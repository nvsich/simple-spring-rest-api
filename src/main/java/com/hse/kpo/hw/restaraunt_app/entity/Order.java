package com.hse.kpo.hw.restaraunt_app.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ElementCollection
    @Column(name = "menu_id")
    private List<Long> menuIds;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "price")
    private Integer price;

}

