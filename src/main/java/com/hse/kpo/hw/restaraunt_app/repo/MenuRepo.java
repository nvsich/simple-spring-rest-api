package com.hse.kpo.hw.restaraunt_app.repo;

import com.hse.kpo.hw.restaraunt_app.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepo extends JpaRepository<Menu, Long> {
}
