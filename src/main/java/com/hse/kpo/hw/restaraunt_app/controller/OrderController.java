package com.hse.kpo.hw.restaraunt_app.controller;

import com.hse.kpo.hw.restaraunt_app.entity.Order;
import com.hse.kpo.hw.restaraunt_app.entity.User;
import com.hse.kpo.hw.restaraunt_app.exception.OrderNotFoundException;
import com.hse.kpo.hw.restaraunt_app.exception.UserNotFoundException;
import com.hse.kpo.hw.restaraunt_app.service.OrderService;
import com.hse.kpo.hw.restaraunt_app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/orders")
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;

    private UserService userService;


    @GetMapping("/all-orders")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Order>> allOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping
    public ResponseEntity<List<Order>> myOrders(Principal principal) {
        Optional<User> currentUser = userService.findByName(principal.getName());

        if (currentUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        var list = currentUser.map(user -> orderService.findAllByUserId(user.getId()));
        return list.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok(null));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Order> newOrder(@RequestBody Order order, Principal principal) {
        Optional<User> currentUser = userService.findByName(principal.getName());

        if (currentUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found exception");
        }

        order.setUserId(currentUser.get().getId());

        return new ResponseEntity<>(orderService.save(order), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id, Principal principal) {
        checkOrderForPrincipal(id, principal);

        orderService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, Order order, Principal principal) {
        checkOrderForPrincipal(id, principal);

        return ResponseEntity.ok(orderService.update(id, order));
    }

    @PostMapping("/pay/{id}")
    public ResponseEntity<Void> payForOrder(@PathVariable Long id, Principal principal) {
        checkOrderForPrincipal(id, principal);
        orderService.payForOrder(id);

        return ResponseEntity.ok().build();
    }

    private void checkOrderForPrincipal(@PathVariable Long id, Principal principal) {
        Optional<Order> order = orderService.findById(id);

        if (order.isEmpty()) {
            throw new OrderNotFoundException("Order with id " + id + " not found");
        }

        Optional<User> currentUser = userService.findByName(principal.getName());
        Optional<User> userFromOrder = userService.findById(order.get().getUserId());

        if (currentUser.isEmpty()
                || userFromOrder.isEmpty()
                || !currentUser.get().getName().equals(userFromOrder.get().getName())
        ) {
            throw new UsernameNotFoundException("User not found exception");
        }
    }
}
