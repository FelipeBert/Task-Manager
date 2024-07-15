package org.FelipeBert.TaskManager.service;

import org.FelipeBert.TaskManager.dtos.UserDtos.UpdatePasswordDto;
import org.FelipeBert.TaskManager.dtos.UserDtos.UserDto;
import org.FelipeBert.TaskManager.dtos.UserDtos.UserRegisterDto;
import org.FelipeBert.TaskManager.model.User;

public interface UserService {
    public UserDto create(UserRegisterDto userRegisterDto);
    public UserDto updatePassword(UpdatePasswordDto updatePasswordDto, User user);
}
