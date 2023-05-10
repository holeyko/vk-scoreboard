package com.holeyko.vkscoreboard.controller;

import com.holeyko.vkscoreboard.dto.validator.CategoryValidator;
import com.holeyko.vkscoreboard.exception.ResourceNotFoundException;
import com.holeyko.vkscoreboard.exception.ValidationException;
import com.holeyko.vkscoreboard.model.Category;
import com.holeyko.vkscoreboard.model.User;
import com.holeyko.vkscoreboard.service.CategoryService;
import com.holeyko.vkscoreboard.service.JwtService;
import com.holeyko.vkscoreboard.service.UserService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/1/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;
    private final JwtService jwtService;
    private final CategoryValidator categoryValidator;

    public CategoryController(CategoryService categoryService, UserService userService, JwtService jwtService, CategoryValidator categoryValidator) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.categoryValidator = categoryValidator;
    }

    @GetMapping
    public List<Category> categories() {
        return categoryService.findAll();
    }

    @PostMapping("/new")
    public Category create(@Valid @RequestBody Category category, BindingResult bindingResult) {
        categoryValidator.validate(category, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return categoryService.save(category);
    }

    @GetMapping("/top/{categoryName}/{count}")
    public List<User> getTopUsersInCategory(@PathVariable("categoryName") String categoryName,
                                            @PathVariable("count") int count
    ) {
        categoryService.findByName(categoryName).orElseThrow(ResourceNotFoundException::new);

        List<User> usersInCategory = new ArrayList<>(userService.sortedUsersByCountSolvedTasksInCategory(categoryName));
        Collections.reverse(usersInCategory);
        return usersInCategory.stream().limit(count).toList();
    }

    @GetMapping("/place/{categoryName}")
    public int getUserPlaceInCategoryTop(@PathVariable("categoryName") String categoryName,
                                         @RequestParam("jwt") String jwt) {
        categoryService.findByName(categoryName).orElseThrow(ResourceNotFoundException::new);

        User user = jwtService.find(jwt).orElseThrow(ResourceNotFoundException::new);
        List<User> usersInCategory = userService.sortedUsersByCountSolvedTasksInCategory(categoryName);
        User foundUser = usersInCategory.stream()
                .filter(categoryUser -> categoryUser.getId() == user.getId())
                .findFirst().orElseThrow(ResourceNotFoundException::new);

        return usersInCategory.size() - usersInCategory.indexOf(foundUser) - 1;
    }
}
