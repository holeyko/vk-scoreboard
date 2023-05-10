package com.holeyko.vkscoreboard.dto.validator;

import com.holeyko.vkscoreboard.dto.UpdateUserCountSolved;
import com.holeyko.vkscoreboard.service.CategoryService;
import com.holeyko.vkscoreboard.service.JwtService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UpdateUserCountSolvedValidator implements Validator {
    private final CategoryService categoryService;
    private final JwtService jwtService;

    public UpdateUserCountSolvedValidator(CategoryService categoryService, JwtService jwtService) {
        this.categoryService = categoryService;
        this.jwtService = jwtService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateUserCountSolved.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof UpdateUserCountSolved updateUserCountSolved) {
            if (categoryService.findByName(updateUserCountSolved.getCategoryName()).isEmpty()) {
                errors.reject("category-not-exists", "category with this name doesn't exist");
            }
            if (jwtService.find(updateUserCountSolved.getJwt()).isEmpty()) {
                errors.reject("user-not-exists", "not found user");
            }
        }
    }
}
