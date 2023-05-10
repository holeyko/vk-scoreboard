package com.holeyko.vkscoreboard.controller;

import com.holeyko.vkscoreboard.dto.UserCredentialsEnter;
import com.holeyko.vkscoreboard.dto.UserDTO;
import com.holeyko.vkscoreboard.dto.validator.UserCredentialsEnterValidator;
import com.holeyko.vkscoreboard.exception.ResourceNotFoundException;
import com.holeyko.vkscoreboard.exception.ValidationException;
import com.holeyko.vkscoreboard.service.JwtService;
import com.holeyko.vkscoreboard.service.UserService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/1/jwt")
public class JwtController {
    private final JwtService jwtService;
    private final UserService userService;
    private final UserCredentialsEnterValidator userCredentialsEnterValidator;

    public JwtController(
            JwtService jwtService,
            UserService userService,
            UserCredentialsEnterValidator userCredentialsEnterValidator
    ) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.userCredentialsEnterValidator = userCredentialsEnterValidator;
    }

    @PostMapping("/auth")
    public String authorization(@Valid @RequestBody UserCredentialsEnter userCredentialsEnter,
                                BindingResult bindingResult) {
        userCredentialsEnterValidator.validate(userCredentialsEnter, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return jwtService.create(userService.findByLoginAndPassword(
                userCredentialsEnter.getLogin(),
                userCredentialsEnter.getPassword()
        ).orElseThrow());
    }

    @GetMapping("/user")
    public UserDTO getUser(@RequestParam("jwt") String jwt) {
        return userService.toDTO(
                jwtService.find(jwt).orElseThrow(ResourceNotFoundException::new)
        );
    }
}
