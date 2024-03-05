package com.hse.kpo.hw.restaraunt_app.service;

import com.hse.kpo.hw.restaraunt_app.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<Order> findAll();

    List<Order> findAllByUserId(Long id);

    Optional<Order> findById(Long id);

    Order save(Order order);

    Order update(Long id, Order order);

    void deleteById(Long id);

    void payForOrder(Long id);

}
