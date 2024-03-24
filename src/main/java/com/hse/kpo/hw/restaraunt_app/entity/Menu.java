package com.hse.kpo.hw.restaraunt_app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Integer price;

    /**
     * Time to cook in minutes.
     */
    @Column(name = "time")
    private Integer time;

    @Column(name = "amount")
    private Integer amount;

}
