package com.hse.kpo.hw.restaraunt_app.service;

import com.hse.kpo.hw.restaraunt_app.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByName(String name);

    User save(User user);

    void deleteById(Long id);
}
