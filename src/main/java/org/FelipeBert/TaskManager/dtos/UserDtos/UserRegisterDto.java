package org.FelipeBert.TaskManager.dtos.UserDtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.FelipeBert.TaskManager.model.enums.UserRole;
import org.FelipeBert.TaskManager.model.enums.UserStatus;

@Getter
public class UserRegisterDto {
    @NotEmpty(message = "Name is mandatory")
    @NotNull(message = "Name is mandatory")
    private String name;

    @NotEmpty(message = "Login is mandatory")
    @NotNull(message = "Login is mandatory")
    private String login;

    @NotEmpty(message = "Email is mandatory")
    @NotNull(message = "Email is mandatory")
    private String email;

    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "The password must contain at least 8 characters")
    private String password;

    private UserStatus userStatus;
    private UserRole userRole;

    public UserRegisterDto(String name, String login, String email, String password,
                           UserStatus userStatus, UserRole userRole) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.password = password;
        this.userStatus = UserStatus.INACTIVE;
        this.userRole = UserRole.USER;
    }

    public void setUserRole(String userRole) {
        this.userRole = UserRole.USER;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = UserStatus.INACTIVE;
    }
}
