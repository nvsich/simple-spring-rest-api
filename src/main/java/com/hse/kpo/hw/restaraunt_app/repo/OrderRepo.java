package com.hse.kpo.hw.restaraunt_app.repo;

import com.hse.kpo.hw.restaraunt_app.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {

    List<Order> findAllByUserId(Long userId);

}
