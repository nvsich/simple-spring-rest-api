package com.hse.kpo.hw.restaraunt_app.service;

import com.hse.kpo.hw.restaraunt_app.entity.Review;

import java.util.List;

public interface ReviewService {

    Review save(Review review);

    List<Review> findAllByMenuId(Long id);
}
