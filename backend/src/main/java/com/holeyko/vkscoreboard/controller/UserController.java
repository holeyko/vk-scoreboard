package com.holeyko.vkscoreboard.controller;

import com.holeyko.vkscoreboard.dto.UpdateUserCountSolved;
import com.holeyko.vkscoreboard.dto.UserCredentialsRegister;
import com.holeyko.vkscoreboard.dto.validator.UpdateUserCountSolvedValidator;
import com.holeyko.vkscoreboard.dto.validator.UserCredentialsRegisterValidator;
import com.holeyko.vkscoreboard.exception.ResourceNotFoundException;
import com.holeyko.vkscoreboard.exception.ValidationException;
import com.holeyko.vkscoreboard.model.User;
import com.holeyko.vkscoreboard.model.UserCategoryStatistic;
import com.holeyko.vkscoreboard.model.UserStatistic;
import com.holeyko.vkscoreboard.service.JwtService;
import com.holeyko.vkscoreboard.service.UserService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.module.ResolutionException;

@RestController
@RequestMapping("/api/1/users")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final UserCredentialsRegisterValidator userCredentialsRegisterValidator;
    private final UpdateUserCountSolvedValidator updateUserCountSolvedValidator;

    public UserController(
            UserService userService,
            JwtService jwtService,
            UserCredentialsRegisterValidator userCredentialsRegisterValidator,
            UpdateUserCountSolvedValidator updateUserCountSolvedValidator) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.userCredentialsRegisterValidator = userCredentialsRegisterValidator;
        this.updateUserCountSolvedValidator = updateUserCountSolvedValidator;
    }

    @PostMapping("/new")
    public String register(@Valid @RequestBody UserCredentialsRegister userCredentialsRegister,
                           BindingResult bindingResult
    ) {
        userCredentialsRegisterValidator.validate(userCredentialsRegister, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return jwtService.create(userService.save(userCredentialsRegister));
    }

    @PutMapping("/countSolved")
    public void setUserCountSolved(@Valid @RequestBody UpdateUserCountSolved updateUserCountSolved,
                                   BindingResult bindingResult) {
        updateUserCountSolvedValidator.validate(updateUserCountSolved, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        User user = jwtService.find(updateUserCountSolved.getJwt()).orElseThrow();
        userService.setUserCountSolvedTasksById(
                user,
                updateUserCountSolved.getCategoryName(),
                updateUserCountSolved.getCountSolved()
        );
    }

    @GetMapping("/statistic")
    public UserStatistic getUserCategoryStatistic(@RequestParam("jwt") String jwt) {
        User user = jwtService.find(jwt).orElseThrow(ResolutionException::new);
        return user.getStatistic();
    }

    @GetMapping("/statistic/categoryStatistic/{id}")
    public UserCategoryStatistic getUserCategoryStatistic(@PathVariable("id") long id,
                                                          @RequestParam("categoryName") String categoryName) {
        User user = userService.findById(id).orElseThrow(ResourceNotFoundException::new);

        for (var categoryStat : user.getStatistic().getCategoryStatistics()) {
            if (categoryName.equals(categoryStat.getCategory().getName())) {
                return categoryStat;
            }
        }

        throw new ResourceNotFoundException();
    }
}
