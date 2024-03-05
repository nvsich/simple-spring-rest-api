package com.hse.kpo.hw.restaraunt_app.service.impl;

import com.hse.kpo.hw.restaraunt_app.entity.Order;
import com.hse.kpo.hw.restaraunt_app.entity.OrderStatus;
import com.hse.kpo.hw.restaraunt_app.entity.Review;
import com.hse.kpo.hw.restaraunt_app.exception.RatingNotFoundException;
import com.hse.kpo.hw.restaraunt_app.repo.OrderRepo;
import com.hse.kpo.hw.restaraunt_app.repo.ReviewRepo;
import com.hse.kpo.hw.restaraunt_app.service.StatsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    private OrderRepo orderRepo;

    private ReviewRepo reviewRepo;

    @Override
    public Integer getRevenue() {
        return orderRepo
                .findAll()
                .stream()
                .filter(order -> order.getOrderStatus().equals(OrderStatus.PAID))
                .map(Order::getPrice)
                .reduce(0, Integer::sum);
    }

    @Override
    public Integer getMenuDishRating(Long id) {
        var allReviews = reviewRepo.findAllByMenuId(id);

        if (allReviews.isEmpty()) {
            throw new RatingNotFoundException("No ratings for menu dish with id " + id);
        }

        return allReviews
                .stream()
                .map(Review::getRating)
                .reduce(0, Integer::sum) / allReviews.size();
    }
}
