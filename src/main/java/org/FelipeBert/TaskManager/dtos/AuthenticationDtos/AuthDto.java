package org.FelipeBert.TaskManager.dtos.AuthenticationDtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AuthDto {
    @NotEmpty(message = "Login is mandatory")
    @NotNull(message = "Login is mandatory")
    private String login;

    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "The password must contain at least 8 characters")
    private String password;

    public AuthDto(String password, String login) {
        this.password = password;
        this.login = login;
    }
}
