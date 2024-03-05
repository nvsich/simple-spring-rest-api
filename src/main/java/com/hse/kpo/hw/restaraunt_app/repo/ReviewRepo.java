package com.hse.kpo.hw.restaraunt_app.repo;

import com.hse.kpo.hw.restaraunt_app.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review, Long> {

    List<Review> findAllByMenuId(Long id);
}
