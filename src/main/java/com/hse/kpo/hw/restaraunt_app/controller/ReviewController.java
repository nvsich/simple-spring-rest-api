package com.hse.kpo.hw.restaraunt_app.controller;

import com.hse.kpo.hw.restaraunt_app.entity.OrderStatus;
import com.hse.kpo.hw.restaraunt_app.entity.Review;
import com.hse.kpo.hw.restaraunt_app.exception.NotEnoughRightsToMakeReview;
import com.hse.kpo.hw.restaraunt_app.service.OrderService;
import com.hse.kpo.hw.restaraunt_app.service.ReviewService;
import com.hse.kpo.hw.restaraunt_app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@AllArgsConstructor
public class ReviewController {

    private ReviewService reviewService;

    private OrderService orderService;

    private UserService userService;

    @GetMapping("/{menuId}")
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long menuId) {
        return ResponseEntity.ok(reviewService.findAllByMenuId(menuId));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Review> newReview(@RequestBody Review review, Principal principal) {
        var user = userService.findByName(principal.getName());

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        var userOrders = orderService.findAllByUserId(user.get().getId());

        var canMakeReview = userOrders
                .stream()
                .anyMatch(order ->
                        order.getMenuIds().contains(review.getMenuId())
                                && order.getOrderStatus().equals(OrderStatus.PAID));

        if (!canMakeReview) {
            throw new NotEnoughRightsToMakeReview("You can't make review to dish " +
                    "that you didn't order or you didn't pay yet.");
        }

        return ResponseEntity.ok(reviewService.save(review));
    }
}
