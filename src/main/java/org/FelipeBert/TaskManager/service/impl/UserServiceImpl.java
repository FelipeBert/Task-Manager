package org.FelipeBert.TaskManager.service.impl;

import org.FelipeBert.TaskManager.dtos.UserDtos.UpdatePasswordDto;
import org.FelipeBert.TaskManager.dtos.UserDtos.UserDto;
import org.FelipeBert.TaskManager.dtos.UserDtos.UserRegisterDto;
import org.FelipeBert.TaskManager.exceptions.*;
import org.FelipeBert.TaskManager.model.Token;
import org.FelipeBert.TaskManager.model.User;
import org.FelipeBert.TaskManager.model.enums.UserStatus;
import org.FelipeBert.TaskManager.repository.TokenRepository;
import org.FelipeBert.TaskManager.repository.UserRepository;
import org.FelipeBert.TaskManager.service.AuthenticationService;
import org.FelipeBert.TaskManager.service.JavaMailService;
import org.FelipeBert.TaskManager.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailService javaMailService;
    private final AuthenticationService authenticationService;
    private final TokenRepository tokenRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           JavaMailService javaMailService, AuthenticationService authenticationService,
                           TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.javaMailService = javaMailService;
        this.authenticationService = authenticationService;
        this.tokenRepository = tokenRepository;
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

        sendEmail(entity);

        return new UserDto(entity.getName(),entity.getCreationDate(), entity.getUserStatus(), entity.getUserRole());
    }

    @Override
    public UserDto updatePassword(UpdatePasswordDto updatePasswordDto, User user) {
        if(user.getUserStatus() == UserStatus.INACTIVE){
            throw new AccountNotActivatedException();
        }
        if(updatePasswordDto.getNewPassword().equals(updatePasswordDto.getCurrentPassword())){
            throw new PasswordException();
        }
        User entity = userRepository.findByLogin(updatePasswordDto.getLogin())
                .orElseThrow(UserAlreadyExists::new);
        if(!entity.getId().equals(user.getId())){
            throw new UserIdDoesNotMatchException();
        }

        if (!passwordEncoder.matches(updatePasswordDto.getCurrentPassword(), entity.getPassword())) {
            throw new IncorrectPasswordException();
        }

        var newPasswordHash = passwordEncoder.encode(updatePasswordDto.getNewPassword());
        entity.setPassword(newPasswordHash);
        userRepository.save(entity);

        return new UserDto(entity.getName(), entity.getCreationDate(), entity.getUserStatus(), entity.getUserRole());
    }

    @Override
    public void validateActivationToken(String token) {
        Token savedToken = tokenRepository.findByToken(token).orElseThrow(TokenNotFoundException::new);
        User user = userRepository.findById(savedToken.getUser().getId()).orElseThrow(UserNotFoundException::new);
        if(LocalDateTime.now().isAfter(savedToken.getExpirationDate())){
            sendEmail(user);
        }
        else{
            user.setUserStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
    }

    private void sendEmail(User user){
        String token = authenticationService.generateActivationToken(user);
        Token newToken = new Token(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
        tokenRepository.save(newToken);
        String to = user.getEmail();
        String subject = "Account Activation";
        String text = "Hello " + user.getName() + ",\n\nPlease activate your account using the following token:\n" + token;
        javaMailService.sendEmail(to, subject, text);
    }
}
