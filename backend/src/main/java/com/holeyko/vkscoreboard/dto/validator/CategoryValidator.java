package com.holeyko.vkscoreboard.dto.validator;

import com.holeyko.vkscoreboard.model.Category;
import com.holeyko.vkscoreboard.service.CategoryService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CategoryValidator implements Validator {
    private final CategoryService categoryService;

    public CategoryValidator(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Category.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof Category category) {
            categoryService.findByName(category.getName())
                    .ifPresent(categ -> errors.reject(
                            "category-already-exists",
                            "category with this name already exists")
                    );
        }
    }
}
