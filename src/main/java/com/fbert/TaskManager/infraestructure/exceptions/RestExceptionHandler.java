package com.fbert.TaskManager.infraestructure.exceptions;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends RuntimeException{

    @ExceptionHandler(JWTCreationException.class)
    private ResponseEntity<String> jwtCreationExceptionHandler(JWTCreationException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error While Generating Token!" + exception.getMessage());
    }

    @ExceptionHandler(JWTVerificationException.class)
    private ResponseEntity<String> jwtVerificationExceptionHandler(JWTVerificationException exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Verifying Token" + exception.getMessage());
    }
}
