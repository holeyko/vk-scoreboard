package com.holeyko.vkscoreboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialsEnter {
    @NotBlank
    @Size(min = 3, max = 64)
    private String login;

    @NotBlank
    @Size(min = 5, max = 32)
    private String password;
}
