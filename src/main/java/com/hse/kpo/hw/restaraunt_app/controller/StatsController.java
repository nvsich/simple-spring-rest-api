package com.hse.kpo.hw.restaraunt_app.controller;

import com.hse.kpo.hw.restaraunt_app.service.StatsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@AllArgsConstructor
public class StatsController {

    private StatsService statsService;

    @GetMapping("/revenue")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Integer> getRevenue() {
        return ResponseEntity.ok(statsService.getRevenue());
    }

    @GetMapping("/rating/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Integer> getDishRating(@PathVariable Long id) {
        return ResponseEntity.ok(statsService.getMenuDishRating(id));
    }
}
