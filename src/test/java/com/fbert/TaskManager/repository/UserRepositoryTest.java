package com.fbert.TaskManager.repository;

import com.fbert.TaskManager.dto.UserRegisterDTO;
import com.fbert.TaskManager.entity.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Must find the login from DB")
    void findByLoginSuceed() {
        String login = "felipito";
        UserRegisterDTO data = new UserRegisterDTO(login, "password");
        this.createUser(data);

        Optional<UserDetails> foundedUser = Optional.ofNullable(this.userRepository.findByLogin(login));

        assertThat(foundedUser.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get user from DB")
    void findByLoginFailure() {
        String login = "new_user";
        Optional<UserDetails> foundedUser = Optional.ofNullable(this.userRepository.findByLogin(login));

        assertThat(foundedUser.isPresent()).isFalse();
    }

    private User createUser(UserRegisterDTO userRegisterDTO){
        String encryptedPassword = new BCryptPasswordEncoder().encode(userRegisterDTO.getPassword());
        User user = new User(userRegisterDTO.getLogin(), encryptedPassword, userRegisterDTO.getRole());
        this.userRepository.save(user);
        return user;
    }
}