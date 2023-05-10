package com.holeyko.vkscoreboard.dto;

import com.holeyko.vkscoreboard.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialsRegister {
    @NotBlank
    @Size(min = 3, max = 64)
    private String login;

    @NotBlank
    @Size(min = 5, max = 32)
    private String password;

    @NotBlank
    @Size(min = 3, max = 32)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 32)
    private String lastName;
}
