package org.FelipeBert.TaskManager.service;

import org.FelipeBert.TaskManager.dtos.AuthenticationDtos.AuthDto;
import org.FelipeBert.TaskManager.dtos.AuthenticationDtos.TokenResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService extends UserDetailsService {
    public TokenResponseDto getToken(AuthDto authDto);
    public String validateToken(String token);

    TokenResponseDto getRefreshedToken(String refreshToken);
}
