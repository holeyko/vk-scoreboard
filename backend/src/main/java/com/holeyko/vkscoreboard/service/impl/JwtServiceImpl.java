package com.holeyko.vkscoreboard.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.holeyko.vkscoreboard.model.User;
import com.holeyko.vkscoreboard.service.JwtService;
import com.holeyko.vkscoreboard.service.UserService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@PropertySource("classpath:application.properties")
public class JwtServiceImpl implements JwtService {
    private static final String USER_ID_CLAIM = "userId";
    private final UserService userService;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;


    public JwtServiceImpl(Environment environment, UserService userService) {
        this.userService = userService;
        this.algorithm = Algorithm.HMAC256(Objects.requireNonNull(environment.getProperty("jwt.salt")));
        this.jwtVerifier = JWT.require(algorithm).build();
    }

    @Override
    public String create(User user) {
        return JWT.create()
                .withClaim(USER_ID_CLAIM, user.getId())
                .sign(algorithm);
    }

    @Override
    public Optional<User> find(String jwt) {
        try {
            return userService.findById(
                    jwtVerifier.verify(jwt).getClaim(USER_ID_CLAIM).asLong()
            );
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }
}
