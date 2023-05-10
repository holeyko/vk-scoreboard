package com.holeyko.vkscoreboard.dto.validator;


import com.holeyko.vkscoreboard.dto.UserCredentialsEnter;
import com.holeyko.vkscoreboard.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserCredentialsEnterValidator implements Validator {
    private final UserService userService;

    public UserCredentialsEnterValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserCredentialsEnter.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof UserCredentialsEnter userCredentialsEnter) {
            if (userService.findByLoginAndPassword(
                    userCredentialsEnter.getLogin(),
                    userCredentialsEnter.getPassword()
            ).isEmpty()) {
                errors.reject("invalid-login-or-password", "invalid login or password");
            }
        }
    }
}
