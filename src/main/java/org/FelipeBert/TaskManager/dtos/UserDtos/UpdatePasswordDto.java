package org.FelipeBert.TaskManager.dtos.UserDtos;

public record UpdatePasswordDto(String login, String currentPassword, String newPassword) {
}
