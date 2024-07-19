package org.FelipeBert.TaskManager.dtos.UserDtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdatePasswordDto{

    @NotEmpty(message = "Name is mandatory")
    @NotNull(message = "Name is mandatory")
    private String login;

    @NotEmpty(message = "the current password field is required")
    @NotNull(message = "the current password field is required")
    @Size(min = 8, message = "The password must contain at least 8 characters")
    private String currentPassword;

    @NotEmpty(message = "The New password field is required")
    @NotNull(message = "The New password field is required")
    @Size(min = 8, message = "The password must contain at least 8 characters")
    private String newPassword;

    public UpdatePasswordDto(String login, String currentPassword, String newPassword) {
        this.login = login;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
