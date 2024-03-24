package com.hse.kpo.hw.restaraunt_app.controller;

import com.hse.kpo.hw.restaraunt_app.entity.User;
import com.hse.kpo.hw.restaraunt_app.exception.UserNotFoundException;
import com.hse.kpo.hw.restaraunt_app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;


    @GetMapping("/me")
    public ResponseEntity<User> me(Principal principal) {
        var user = userService.findByName(principal.getName());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Username not found. Consider re-login");
        }
        return ResponseEntity.ok(user.get());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> allUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> userById(@PathVariable Long id) {
        var user = userService.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("No user with given id " + id);
        }
        return ResponseEntity.ok(user.get());
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
