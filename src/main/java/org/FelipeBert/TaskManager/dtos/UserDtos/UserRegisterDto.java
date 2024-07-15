package org.FelipeBert.TaskManager.dtos.UserDtos;

import lombok.Getter;
import org.FelipeBert.TaskManager.model.enums.UserRole;
import org.FelipeBert.TaskManager.model.enums.UserStatus;

@Getter
public class UserRegisterDto {
    private String name;
    private String login;
    private String email;
    private String password;
    private UserStatus userStatus;
    private UserRole userRole;

    public UserRegisterDto(String name, String login, String email, String password,
                           UserStatus userStatus, UserRole userRole) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.password = password;
        this.userStatus = userStatus;
        this.userRole = UserRole.valueOf("USER");
    }

    public void setUserRole(String userRole) {
        this.userRole = UserRole.valueOf("USER");
    }
}
