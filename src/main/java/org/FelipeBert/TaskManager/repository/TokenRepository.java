package org.FelipeBert.TaskManager.repository;

import org.FelipeBert.TaskManager.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByToken(String token);
}
