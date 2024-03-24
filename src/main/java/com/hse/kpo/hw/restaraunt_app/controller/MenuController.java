package com.hse.kpo.hw.restaraunt_app.controller;

import com.hse.kpo.hw.restaraunt_app.entity.Menu;
import com.hse.kpo.hw.restaraunt_app.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@AllArgsConstructor
public class MenuController {

    private MenuService menuService;


    @GetMapping
    @Operation(summary = "Watch all menu list")
    @ApiResponse(responseCode = "200", description = "Successfully got all menu")
    public ResponseEntity<List<Menu>> allMenu() {
        return ResponseEntity.ok(menuService.findAll());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Menu> addMeal(@RequestBody Menu menu) {
        return ResponseEntity.ok(menuService.save(menu));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Menu> editMeal(@PathVariable Long id, @RequestBody Menu menu) {
        return ResponseEntity.ok(menuService.update(id, menu));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) {
        menuService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
