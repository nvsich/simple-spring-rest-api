package com.hse.kpo.hw.restaraunt_app.service.impl;

import com.hse.kpo.hw.restaraunt_app.entity.User;
import com.hse.kpo.hw.restaraunt_app.exception.UserNotFoundException;
import com.hse.kpo.hw.restaraunt_app.repo.UserRepo;
import com.hse.kpo.hw.restaraunt_app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;

    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepo.findByName(name);
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public void deleteById(Long id) {
        if (userRepo.findById(id).isEmpty()) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userRepo.deleteById(id);
    }
}

