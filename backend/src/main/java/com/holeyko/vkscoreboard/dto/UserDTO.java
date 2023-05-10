package com.holeyko.vkscoreboard.dto;

import com.holeyko.vkscoreboard.model.Role;
import com.holeyko.vkscoreboard.model.UserStatistic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private long id;
    private String login;
    private String firstName;
    private String lastName;
    private Set<Role> roles = new HashSet<>();
    private UserStatistic statistic;
}
