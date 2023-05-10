package com.holeyko.vkscoreboard.service.impl;

import com.google.common.hash.Hashing;
import com.holeyko.vkscoreboard.dto.UserCredentialsRegister;
import com.holeyko.vkscoreboard.dto.UserDTO;
import com.holeyko.vkscoreboard.exception.ResourceNotFoundException;
import com.holeyko.vkscoreboard.model.*;
import com.holeyko.vkscoreboard.repository.RoleRepository;
import com.holeyko.vkscoreboard.repository.CategoryRepository;
import com.holeyko.vkscoreboard.repository.UserRepository;
import com.holeyko.vkscoreboard.service.UserService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:application.properties")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final String hashSalt;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            CategoryRepository categoryRepository, Environment environment
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.hashSalt = environment.getProperty("password.hash.salt");
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPasswordSha(login, hashPassword(password));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    private String hashPassword(String password) {
        return Hashing.sha256()
                .hashString(password + hashSalt, StandardCharsets.UTF_8)
                .toString();
    }

    @Override
    public User save(UserCredentialsRegister userCredentialsRegister) {
        var user = new User();
        var userStatistic = new UserStatistic();
        user.setLogin(userCredentialsRegister.getLogin());
        user.setPasswordSha(hashPassword(userCredentialsRegister.getPassword()));
        user.setFirstName(userCredentialsRegister.getFirstName());
        user.setLastName(userCredentialsRegister.getLastName());
        user.setStatistic(userStatistic);
        userStatistic.setUser(user);

        return save(user);
    }

    @Override
    public User save(User user) {
        Set<Role> userRoles = Optional.ofNullable(user.getRoles())
                .map(roles -> roles.stream().map(
                        role -> roleRepository.findByName(role.getName())
                                .orElseGet(() -> roleRepository.save(role))
                ).collect(Collectors.toSet())).orElse(null);
        user.setRoles(userRoles);

        return userRepository.save(user);
    }

    @Override
    public UserDTO toDTO(User user) {
        var userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setRoles(user.getRoles());
        userDto.setStatistic(user.getStatistic());

        return userDto;
    }

    @Override
    public List<User> findAllByCategoryName(String categoryName) {
        return userRepository.findAllByCategoryName(categoryName);
    }

    @Override
    public void setUserCountSolvedTasksById(User user, String categoryName, int countSolved) {
        Category category = categoryRepository.findByName(categoryName).orElseThrow(ResourceNotFoundException::new);

        UserCategoryStatistic userCategoryStatistic = user.getStatistic().getCategoryStatistics().stream()
                .filter(categoryStatistic -> categoryName.equals(
                        categoryStatistic.getCategory().getName())
                ).findFirst().orElseGet(() -> {
                    var categoryStatistic = new UserCategoryStatistic();
                    categoryStatistic.setCategory(category);
                    user.getStatistic().addCategoryStatistic(categoryStatistic);
                    categoryStatistic.setUserStatistic(user.getStatistic());

                    return categoryStatistic;
                });
        userCategoryStatistic.setCountSolved(countSolved);
        save(user);
    }

    @Override
    public List<User> sortedUsersByCountSolvedTasksInCategory(String categoryName) {
        Function<User, Integer> userCountSolved = user -> user.getStatistic().getCategoryStatistics().stream()
                .filter(categoryStatistic -> categoryName.equals(
                        categoryStatistic.getCategory().getName()
                )).findFirst().orElseThrow().getCountSolved();

        return findAllByCategoryName(categoryName).stream()
                .sorted((f, s) -> {
                    int countF = userCountSolved.apply(f);
                    int countS = userCountSolved.apply(s);
                    return Integer.compare(countF, countS);
                }).toList();
    }
}