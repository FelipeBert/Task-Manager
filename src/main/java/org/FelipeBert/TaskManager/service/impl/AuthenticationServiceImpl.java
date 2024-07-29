package org.FelipeBert.TaskManager.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.FelipeBert.TaskManager.dtos.AuthenticationDtos.AuthDto;
import org.FelipeBert.TaskManager.dtos.AuthenticationDtos.TokenResponseDto;
import org.FelipeBert.TaskManager.model.User;
import org.FelipeBert.TaskManager.model.enums.UserRole;
import org.FelipeBert.TaskManager.repository.UserRepository;
import org.FelipeBert.TaskManager.service.AuthenticationService;
import org.FelipeBert.TaskManager.exceptions.UserNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);
    private final UserRepository userRepository;

    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${auth.jwt.token.secret}")
    private String secretKey;

    @Value("${auth.jwt.token.expiration}")
    private Integer tokenExpirationTime;

    @Value("${auth.jwt.refresh-token.expiration}")
    private Integer tokenExpirationRefresh;

    @Override
    public TokenResponseDto getToken(AuthDto authDto) {
        logger.info("Generating tokens for user with login {}", authDto.getLogin());

        User optionalUser = userRepository.findByLogin(authDto.getLogin())
                .orElseThrow(() -> {
                    logger.error("User with login {} not found", authDto.getLogin());
                    return new UserNotFoundException();
                });

        String token = generateToken(optionalUser, tokenExpirationTime);
        String refreshToken = generateToken(optionalUser, tokenExpirationRefresh);

        logger.info("Tokens generated successfully for user with login {}", authDto.getLogin());

        return TokenResponseDto.builder().token(token).refreshToken(refreshToken).build();
    }

    public String generateToken(User entity, Integer expiration) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(entity.getUsername())
                    .withExpiresAt(generateExpirationTime(expiration))
                    .sign(algorithm);

            logger.info("Token generated for user {}", entity.getUsername());
            return token;
        } catch (JWTCreationException exception) {
            logger.error("Error while generating token: {}", exception.getMessage());
            throw new RuntimeException("Error while generating token! " + exception.getMessage());
        }
    }

    @Override
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            String subject = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

            logger.info("Token validated successfully. Subject: {}", subject);
            return subject;
        } catch (JWTVerificationException e) {
            logger.error("Error while validating token: {}", e.getMessage());
            throw new RuntimeException("Error while validating token! " + e.getMessage());
        }
    }

    @Override
    public String generateActivationToken(User user) {
        logger.info("Generating activation token for user {}", user.getUsername());
        return generateToken(user, 15);
    }

    @Override
    public TokenResponseDto getRefreshedToken(String refreshToken) {
        logger.info("Refreshing token using refresh token {}", refreshToken);

        String login = validateToken(refreshToken);
        User entity = userRepository.findByLogin(login)
                .orElseThrow(() -> {
                    logger.error("User with login {} not found", login);
                    return new UserNotFoundException();
                });

        var authentication = new UsernamePasswordAuthenticationToken(entity, null, entity.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String newToken = generateToken(entity, tokenExpirationTime);
        String newRefreshToken = generateToken(entity, tokenExpirationRefresh);

        logger.info("Tokens refreshed successfully for user {}", entity.getUsername());

        return TokenResponseDto.builder().token(newToken).refreshToken(newRefreshToken).build();
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UserNotFoundException {
        logger.info("Loading user details for login {}", login);

        User entity = userRepository.findByLogin(login)
                .orElseThrow(() -> {
                    logger.error("User with login {} not found", login);
                    return new UserNotFoundException();
                });

        return User.builder()
                .login(entity.getLogin())
                .password(entity.getPassword())
                .userRole(UserRole.valueOf(entity.getUserRole().name()))
                .build();
    }

    private Instant generateExpirationTime(Integer expiration) {
        return LocalDateTime.now()
                .plusHours(expiration)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}