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
    public TokenResponseDto getToken(AuthDto authDto){
        User optionalUser = userRepository.findByLogin(authDto.getLogin()).orElseThrow(UserNotFoundException::new);
        return TokenResponseDto.builder().token(generateToken(optionalUser, tokenExpirationTime))
                .refreshToken(generateToken(optionalUser, tokenExpirationRefresh))
                .build();
    }

    public  String generateToken(User entity, Integer expiration) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(entity.getUsername())
                    .withExpiresAt(generateExpirationTime(expiration))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao tentar gerar o token! " +exception.getMessage());
        }
    }

    @Override
    public String validateToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException e){
            throw new RuntimeException("Erro ao tentar validar o token! " + e.getMessage());
        }
    }

    @Override
    public String generateActivationToken(User user) {
        return generateToken(user, 15);
    }

    @Override
    public TokenResponseDto getRefreshedToken(String refreshToken) {
        String login = validateToken(refreshToken);
        User entity = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        var authentication = new UsernamePasswordAuthenticationToken(entity, null, entity.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return TokenResponseDto.builder().token(generateToken(entity, tokenExpirationTime))
                .refreshToken(generateToken(entity, tokenExpirationRefresh))
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UserNotFoundException {
        User entity = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);

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
