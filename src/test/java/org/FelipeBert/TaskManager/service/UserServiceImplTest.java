package org.FelipeBert.TaskManager.service;

import org.FelipeBert.TaskManager.dtos.UserDtos.UpdatePasswordDto;
import org.FelipeBert.TaskManager.dtos.UserDtos.UserDto;
import org.FelipeBert.TaskManager.dtos.UserDtos.UserRegisterDto;
import org.FelipeBert.TaskManager.exceptions.PasswordException;
import org.FelipeBert.TaskManager.exceptions.UserAlreadyExists;
import org.FelipeBert.TaskManager.model.User;
import org.FelipeBert.TaskManager.model.enums.UserRole;
import org.FelipeBert.TaskManager.model.enums.UserStatus;
import org.FelipeBert.TaskManager.repository.UserRepository;
import org.FelipeBert.TaskManager.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceImplTest {

    @Test
    public void test_create_user_with_valid_details() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository, passwordEncoder);

        UserRegisterDto userRegisterDto = new UserRegisterDto("John Doe", "johndoe", "john@example.com", "password123", UserStatus.ACTIVE, UserRole.USER);
        Mockito.when(userRepository.findByLogin(userRegisterDto.getLogin())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(userRegisterDto.getPassword())).thenReturn("hashedPassword");

        UserDto result = userService.create(userRegisterDto);

        assertNotNull(result);
        assertEquals("John Doe", result.name());
        assertEquals(UserStatus.ACTIVE, result.userStatus());
        assertEquals(UserRole.USER, result.userRole());
    }

    @Test
    public void test_create_user_with_existing_login() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository, passwordEncoder);

        UserRegisterDto userRegisterDto = new UserRegisterDto("John Doe", "johndoe", "john@example.com", "password123", UserStatus.ACTIVE, UserRole.USER);
        User existingUser = new User("Jane Doe", "johndoe", "jane@example.com", "password123", LocalDateTime.now(), LocalDateTime.now(), UserStatus.ACTIVE, UserRole.USER, new ArrayList<>());
        Mockito.when(userRepository.findByLogin(userRegisterDto.getLogin())).thenReturn(Optional.of(existingUser));

        assertThrows(UserAlreadyExists.class, () -> {
            userService.create(userRegisterDto);
        });
    }

    @Test
    public void test_successful_password_update() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository, passwordEncoder);

        User user = new User();
        user.setId("123");
        user.setPassword("encodedCurrentPassword");

        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto("userLogin", "currentPassword", "newPassword");

        Mockito.when(userRepository.findByLogin("userLogin")).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("currentPassword", "encodedCurrentPassword")).thenReturn(true);
        Mockito.when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        UserDto result = userService.updatePassword(updatePasswordDto, user);

        Mockito.verify(userRepository).save(user);
        Assertions.assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    public void test_password_exception_same_passwords() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository, passwordEncoder);

        User user = new User();
        user.setId("123");

        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto("userLogin", "samePassword", "samePassword");

        Assertions.assertThrows(PasswordException.class, () -> {
            userService.updatePassword(updatePasswordDto, user);
        });
    }
}
