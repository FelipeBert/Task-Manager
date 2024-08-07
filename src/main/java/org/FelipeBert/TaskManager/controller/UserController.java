package org.FelipeBert.TaskManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.FelipeBert.TaskManager.dtos.UserDtos.UpdatePasswordDto;
import org.FelipeBert.TaskManager.dtos.UserDtos.UserDto;
import org.FelipeBert.TaskManager.dtos.UserDtos.UserRegisterDto;
import org.FelipeBert.TaskManager.model.User;
import org.FelipeBert.TaskManager.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/user", produces = {"application/json"})
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "422", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Error creating user"),
    })
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserRegisterDto userRegisterDto){
        String correlationId = getCorrelationId();
        logger.info("Correlation ID: {} - Creating user {} with email {}",
                correlationId, userRegisterDto.getName(), userRegisterDto.getEmail());
        return ResponseEntity.ok(userService.create(userRegisterDto));
    }

    @Operation(summary = "Update information for an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User information updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Prohibited, perform authentication"),
            @ApiResponse(responseCode = "422", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Error updating user information"),
    })
    @PutMapping
    public ResponseEntity<UserDto> updatePassword(@RequestBody @Valid UpdatePasswordDto updatePasswordDto){
        String correlationId = getCorrelationId();
        if(isUserAuthenticated()){
            logger.info("Correlation ID: {} - Updating Password for user {}",
                    correlationId, updatePasswordDto.getLogin());
            return ResponseEntity.ok(userService.updatePassword(updatePasswordDto, getCurrentUser()));
        }
        else{
            logger.warn("Correlation ID: {} - User not authenticated. Cannot update password for user {}", correlationId, updatePasswordDto.getLogin());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<Void> activateAccount(@RequestParam @Valid String token){
        String correlationId = getCorrelationId();
        logger.info("Correlation ID: {} - Activating account with token {}", correlationId, token);
        userService.validateActivationToken(token);
        return ResponseEntity.ok().body(null);
    }

    private boolean isUserAuthenticated(){
        Authentication authenticateUser = SecurityContextHolder.getContext().getAuthentication();
        return authenticateUser != null && authenticateUser.getPrincipal() instanceof UserDetails;
    }

    private User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private String getCorrelationId(){
        return UUID.randomUUID().toString();
    }
}
