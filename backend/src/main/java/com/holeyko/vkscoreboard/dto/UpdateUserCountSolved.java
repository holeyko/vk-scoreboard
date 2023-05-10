package com.holeyko.vkscoreboard.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserCountSolved {
    private String jwt;

    @NotBlank
    private String categoryName;

    @Min(0)
    private int countSolved;
}
