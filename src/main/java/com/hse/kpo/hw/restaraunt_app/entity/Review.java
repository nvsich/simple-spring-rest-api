package com.hse.kpo.hw.restaraunt_app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "text")
    private String text;
}
