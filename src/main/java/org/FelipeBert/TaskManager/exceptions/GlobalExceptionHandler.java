package org.FelipeBert.TaskManager.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CannotDeleteTaskException.class)
    public ResponseEntity<String> handleCannotDeleteTaskException(CannotDeleteTaskException ex){
        return ResponseEntity.badRequest().body("You cannot delete this task");
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<String> handleIncorrectPasswordException(IncorrectPasswordException ex){
        return ResponseEntity.badRequest().body("Password does not match");
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<String> handlePasswordException(PasswordException ex){
        return ResponseEntity.badRequest().body("The new password cannot be the same as the current password.");
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex){
        return ResponseEntity.badRequest().body("Task not Found!");
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExists ex){
        return ResponseEntity.badRequest().body("User Already Exists");
    }

    @ExceptionHandler(UserIdDoesNotMatchException.class)
    public ResponseEntity<String> handleUserIdDoesNotMatchException(UserIdDoesNotMatchException ex){
        return ResponseEntity.badRequest().body("User ID does not Match!");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex){
        return ResponseEntity.badRequest().body("User Not Found!");
    }
}
