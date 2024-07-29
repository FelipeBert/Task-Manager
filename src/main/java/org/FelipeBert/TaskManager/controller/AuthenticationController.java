package org.FelipeBert.TaskManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.FelipeBert.TaskManager.dtos.AuthenticationDtos.AuthDto;
import org.FelipeBert.TaskManager.dtos.AuthenticationDtos.RefreshTokenDto;
import org.FelipeBert.TaskManager.dtos.AuthenticationDtos.TokenResponseDto;
import org.FelipeBert.TaskManager.service.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger logger = LogManager.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(AuthenticationService authenticationService, AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Performs user login and returns a Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token successfully obtained"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "422", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Error validating user"),
    })
    @PostMapping
    public ResponseEntity<TokenResponseDto> authenticate(@RequestBody @Valid AuthDto authDto){
        String correlationId = getCorrelationId();
        logger.info("Correlation ID: {} - Authenticating user with login {}", correlationId, authDto.getLogin());

        UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(authDto.getLogin(), authDto.getPassword());
        authenticationManager.authenticate(userAuthentication);

        TokenResponseDto token = authenticationService.getToken(authDto);
        logger.info("Correlation ID: {} - User {} authenticated successfully", correlationId, authDto.getLogin());

        return ResponseEntity.ok(token);

    }

    @Operation(summary = "Update user's expired token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Prohibited, perform authentication"),
            @ApiResponse(responseCode = "422", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Error validating token"),
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody @Valid RefreshTokenDto refreshTokenDto){
        String correlationId = getCorrelationId();
        logger.info("Correlation ID: {} - Refreshing token using refreshToken {}", correlationId, refreshTokenDto.refreshToken());

        TokenResponseDto token = authenticationService.getRefreshedToken(refreshTokenDto.refreshToken());
        logger.info("Correlation ID: {} - Token updated successfully", correlationId);

        return ResponseEntity.ok(token);
    }

    private String getCorrelationId(){
        return UUID.randomUUID().toString();
    }
}