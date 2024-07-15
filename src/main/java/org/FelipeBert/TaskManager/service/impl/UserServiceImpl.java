package org.FelipeBert.TaskManager.service.impl;

import org.FelipeBert.TaskManager.dtos.UserDtos.UpdatePasswordDto;
import org.FelipeBert.TaskManager.dtos.UserDtos.UserDto;
import org.FelipeBert.TaskManager.dtos.UserDtos.UserRegisterDto;
import org.FelipeBert.TaskManager.exceptions.IncorrectPasswordException;
import org.FelipeBert.TaskManager.exceptions.PasswordException;
import org.FelipeBert.TaskManager.exceptions.UserIdDoesNotMatchException;
import org.FelipeBert.TaskManager.model.User;
import org.FelipeBert.TaskManager.repository.UserRepository;
import org.FelipeBert.TaskManager.service.UserService;
import org.FelipeBert.TaskManager.exceptions.UserAlreadyExists;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto create(UserRegisterDto userRegisterDto) {
        Optional<User> checkIfUserAlreadyExist = userRepository.findByLogin(userRegisterDto.getLogin());

        if(checkIfUserAlreadyExist.isPresent()){
            throw new UserAlreadyExists();
        }

        var passwordHash = passwordEncoder.encode(userRegisterDto.getPassword());

        User entity = new User(userRegisterDto.getName(), userRegisterDto.getLogin(), userRegisterDto.getEmail(),
                passwordHash, LocalDateTime.now(), LocalDateTime.now(), userRegisterDto.getUserStatus(),
                userRegisterDto.getUserRole(), new ArrayList<>());

        userRepository.save(entity);

        return new UserDto(entity.getName(),entity.getCreationDate(), entity.getUserStatus(), entity.getUserRole());
    }

    @Override
    public UserDto updatePassword(UpdatePasswordDto updatePasswordDto, User user) {
        if(updatePasswordDto.newPassword().equals(updatePasswordDto.currentPassword())){
            throw new PasswordException();
        }
        if(updatePasswordDto.currentPassword().isEmpty()){
            throw new PasswordException();
        }
        if(updatePasswordDto.newPassword().isEmpty()){
            throw new PasswordException();
        }
        User entity = userRepository.findByLogin(updatePasswordDto.login())
                .orElseThrow(UserAlreadyExists::new);
        if(!entity.getId().equals(user.getId())){
            throw new UserIdDoesNotMatchException();
        }

        if (!passwordEncoder.matches(updatePasswordDto.currentPassword(), entity.getPassword())) {
            throw new IncorrectPasswordException();
        }

        var newPasswordHash = passwordEncoder.encode(updatePasswordDto.newPassword());
        entity.setPassword(newPasswordHash);
        userRepository.save(entity);

        return new UserDto(entity.getName(), entity.getCreationDate(), entity.getUserStatus(), entity.getUserRole());
    }
}
