package org.FelipeBert.TaskManager.dtos.UserDtos;

import org.FelipeBert.TaskManager.model.enums.UserRole;
import org.FelipeBert.TaskManager.model.enums.UserStatus;

import java.time.LocalDateTime;

public record UserDto(String name, LocalDateTime creationDate, UserStatus userStatus, UserRole userRole) {
}
