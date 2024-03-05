package com.hse.kpo.hw.restaraunt_app.service.impl;

import com.hse.kpo.hw.restaraunt_app.entity.Review;
import com.hse.kpo.hw.restaraunt_app.repo.ReviewRepo;
import com.hse.kpo.hw.restaraunt_app.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepo reviewRepo;

    @Override
    public Review save(Review review) {
        return reviewRepo.save(review);
    }

    @Override
    public List<Review> findAllByMenuId(Long id) {
        return reviewRepo.findAllByMenuId(id);
    }
}
