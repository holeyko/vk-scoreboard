package com.holeyko.vkscoreboard.service;

import com.holeyko.vkscoreboard.dto.UserCredentialsRegister;
import com.holeyko.vkscoreboard.dto.UserDTO;
import com.holeyko.vkscoreboard.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(long id);

    Optional<User> findByLogin(String login);

    Optional<User> findByLoginAndPassword(String login, String password);

    List<User> findAll();

    User save(UserCredentialsRegister userCredentialsRegister);
    User save(User user);

    UserDTO toDTO(User user);

    List<User> findAllByCategoryName(String categoryName);

    void setUserCountSolvedTasksById(User user, String categoryName, int countSolved);

    List<User> sortedUsersByCountSolvedTasksInCategory(String categoryName);
}
