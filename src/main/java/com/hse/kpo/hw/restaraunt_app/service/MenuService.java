package com.hse.kpo.hw.restaraunt_app.service;

import com.hse.kpo.hw.restaraunt_app.entity.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuService {

    List<Menu> findAll();

    Optional<Menu> findById(Long id);

    Menu save(Menu menu);

    Menu update(Long id, Menu menu);

    void deleteById(Long id);

}
