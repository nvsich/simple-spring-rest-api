package com.hse.kpo.hw.restaraunt_app.service.impl;

import com.hse.kpo.hw.restaraunt_app.entity.Menu;
import com.hse.kpo.hw.restaraunt_app.exception.MenuNotFoundException;
import com.hse.kpo.hw.restaraunt_app.repo.MenuRepo;
import com.hse.kpo.hw.restaraunt_app.service.MenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MenuServiceImpl implements MenuService {

    private MenuRepo menuRepo;

    @Override
    public List<Menu>
    findAll() {
        return menuRepo.findAll();
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return menuRepo.findById(id);
    }

    @Override
    public Menu save(Menu menu) {
        return menuRepo.save(menu);
    }

    @Override
    @Transactional
    public Menu update(Long id, Menu menu) {
        if (menuRepo.findById(id).isEmpty()) {
            throw new MenuNotFoundException("Menu with id " + id + " not found");
        }

        var currentMenu = menuRepo.findById(id).get();

        menuRepo.findById(id).ifPresent(toUpdate -> {
            toUpdate.setName(menu.getName() != null ? menu.getName() : currentMenu.getName());
            toUpdate.setPrice(menu.getPrice() != null ? menu.getPrice() : currentMenu.getPrice());
            toUpdate.setTime(menu.getTime() != null ? menu.getTime() : currentMenu.getTime());
            toUpdate.setAmount(menu.getAmount() != null ? menu.getAmount() : currentMenu.getAmount());
        });

        return menuRepo.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {
        if (menuRepo.findById(id).isEmpty()) {
            throw new MenuNotFoundException("Menu with id " + id + " not found");
        }

        menuRepo.deleteById(id);
    }
}
