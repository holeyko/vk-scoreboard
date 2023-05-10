package com.holeyko.vkscoreboard.dto.validator;


import com.holeyko.vkscoreboard.dto.UserCredentialsRegister;
import com.holeyko.vkscoreboard.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserCredentialsRegisterValidator implements Validator {
    private final UserService userService;

    public UserCredentialsRegisterValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserCredentialsRegister.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof UserCredentialsRegister userCredentialsRegister) {
            if (userService.findByLogin(userCredentialsRegister.getLogin())
                    .isPresent()) {
                errors.rejectValue("login", "login-already-exists", "login already exists");
            }
        }
    }
}
