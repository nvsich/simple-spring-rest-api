package com.hse.kpo.hw.restaraunt_app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "roles")
    private String roles;

}
