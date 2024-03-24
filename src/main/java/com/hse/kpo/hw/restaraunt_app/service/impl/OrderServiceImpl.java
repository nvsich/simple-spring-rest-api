package com.hse.kpo.hw.restaraunt_app.service.impl;

import com.hse.kpo.hw.restaraunt_app.entity.Menu;
import com.hse.kpo.hw.restaraunt_app.entity.Order;
import com.hse.kpo.hw.restaraunt_app.entity.OrderStatus;
import com.hse.kpo.hw.restaraunt_app.exception.MenuNotFoundException;
import com.hse.kpo.hw.restaraunt_app.exception.NotEnoughResourcesException;
import com.hse.kpo.hw.restaraunt_app.exception.OrderNotFoundException;
import com.hse.kpo.hw.restaraunt_app.exception.OrderProcessingException;
import com.hse.kpo.hw.restaraunt_app.repo.MenuRepo;
import com.hse.kpo.hw.restaraunt_app.repo.OrderRepo;
import com.hse.kpo.hw.restaraunt_app.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;


@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepo orderRepo;

    private MenuRepo menuRepo;

    private ExecutorService executorService;

    @Override
    public List<Order> findAll() {
        return orderRepo.findAll();
    }

    @Override
    public List<Order> findAllByUserId(Long id) {
        return orderRepo.findAllByUserId(id);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepo.findById(id);
    }

    @Override
    @Transactional
    public Order save(Order order) {
        order.setOrderStatus(OrderStatus.RECEIVED);
        order.setPrice(getOverallOrderPrice(order));

        if (!isEnoughMenuAmountForOrder(order)) {
            throw new NotEnoughResourcesException("Not enough menu dishes for order");
        }

        decreaseMenuAmountByOrder(order);

        var savedOrder = orderRepo.save(order);

        executorService.submit(() -> processOrder(savedOrder));

        return savedOrder;
    }

    @Override
    @Transactional
    public Order update(Long id, Order newOrder) {
        Optional<Order> orderToUpdate = orderRepo.findById(id);

        if (orderToUpdate.isEmpty()) {
            throw new OrderNotFoundException("Order with id " + id + " not found");
        }

        var order = orderToUpdate.get();

        if (order.getOrderStatus().equals(OrderStatus.IN_PROGRESS)) {
            throw new OrderProcessingException("Order with id " + id + " is cooking. Can't update it");
        }

        if (order.getOrderStatus().equals(OrderStatus.READY)
                || order.getOrderStatus().equals(OrderStatus.PAID)) {
            throw new OrderProcessingException("Order with id " + id + " is ready. Can't cancel it");
        }

        newOrder.setPrice(getOverallOrderPrice(newOrder));

        orderRepo.findById(id).ifPresent(toUpdate -> {
            toUpdate.setPrice(newOrder.getPrice());
            toUpdate.setMenuIds(newOrder.getMenuIds());
        });

        return orderRepo.findById(id).get();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<Order> orderToDelete = orderRepo.findById(id);

        if (orderToDelete.isEmpty()) {
            throw new OrderProcessingException("Order with id " + id + " not found");
        }

        var order = orderToDelete.get();

        if (order.getOrderStatus().equals(OrderStatus.IN_PROGRESS)) {
            throw new OrderProcessingException("Order with id " + id + " is cooking. Can't cancel it");
        }

        if (order.getOrderStatus().equals(OrderStatus.READY)
                || order.getOrderStatus().equals(OrderStatus.PAID)) {
            throw new OrderProcessingException("Order with id " + id + " is ready. Can't cancel it");
        }

        /*
         * If order was cancelled by user, we won't restore amount of used meals in menu.
         * It is considered, that these are used products, that can't be used again.
         */
        orderRepo.deleteById(id);
    }

    @Override
    public void payForOrder(Long id) {
        Optional<Order> order = orderRepo.findById(id);

        if (order.isEmpty()) {
            throw new OrderNotFoundException("Order with id " + id + " not found");
        }

        Order toUpdate = order.get();

        if (toUpdate.getOrderStatus().equals(OrderStatus.RECEIVED)
                || order.get().getOrderStatus().equals(OrderStatus.IN_PROGRESS)) {
            throw new OrderProcessingException("Order with id " + id + " is not ready yet");
        }

        if (toUpdate.getOrderStatus().equals(OrderStatus.PAID)) {
            throw new OrderProcessingException("Order with id " + id + " has already been paid");
        }

        toUpdate.setOrderStatus(OrderStatus.PAID);
        orderRepo.save(toUpdate);
    }

    private void processOrder(Order order) {
        var cookingTime = getOverallOrderTime(order);

        order.setOrderStatus(OrderStatus.IN_PROGRESS);

        try {
            Thread.sleep(cookingTime * 1000 * 60);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        order.setOrderStatus(OrderStatus.READY);
        orderRepo.save(order);
    }

    private Integer getOverallOrderPrice(Order order) {
        return order
                .getMenuIds()
                .stream()
                .map(menuId ->
                        menuRepo.findById(menuId)
                                .orElseThrow(() -> new MenuNotFoundException(
                                        "Menu dish with id " + menuId + " not found")))
                .map(Menu::getPrice)
                .reduce(0, Integer::sum);
    }

    private Integer getOverallOrderTime(Order order) {
        return order
                .getMenuIds()
                .stream()
                .map(menuId ->
                        menuRepo.findById(menuId)
                                .orElseThrow(() -> new MenuNotFoundException(
                                        "Menu dish with id " + menuId + " not found")))
                .map(Menu::getTime)
                .reduce(0, Integer::sum);
    }

    private boolean isEnoughMenuAmountForOrder(Order order) {
        return menuRepo
                .findAllById(order.getMenuIds())
                .stream()
                .allMatch(menu -> menu.getAmount() > 0);
    }

    private void decreaseMenuAmountByOrder(Order order) {
        menuRepo.findAllById(order.getMenuIds())
                .forEach(menu -> menu.setAmount(menu.getAmount() - 1));
    }
}
