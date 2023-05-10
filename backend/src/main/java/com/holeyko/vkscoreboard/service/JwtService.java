package com.holeyko.vkscoreboard.service;

import com.holeyko.vkscoreboard.model.User;

import java.util.Optional;

public interface JwtService {
    String create(User user);

    Optional<User> find(String jwt);
}
