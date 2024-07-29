package org.FelipeBert.TaskManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskCreationDto;
import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskDeleteDto;
import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskDto;
import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskUpdateDto;
import org.FelipeBert.TaskManager.model.User;
import org.FelipeBert.TaskManager.service.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private static final Logger logger = LogManager.getLogger(TaskController.class);
    public final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Create a new Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Prohibited, perform authentication"),
            @ApiResponse(responseCode = "422", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Error creating task"),
    })
    @PostMapping
    public ResponseEntity<TaskDto> create(@RequestBody @Valid TaskCreationDto taskCreationDto){
        String correlationId = getCorrelationId();
        if(isUserAuthenticated()){
            logger.info("Correlation ID: {} - User with Id {} Creating a new task {}",
                    correlationId, taskCreationDto.getUserId(), taskCreationDto.getTitulo());
            return ResponseEntity.ok(taskService.create(taskCreationDto, getCurrentUser()));
        }
        else{
            logger.warn("Correlation ID: {} - User not authenticated. Cannot create task for user {}",
                    correlationId, taskCreationDto.getUserId());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @Operation(summary = "Get all User Tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks successfully obtained"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Prohibited, perform authentication"),
            @ApiResponse(responseCode = "422", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "error getting tasks"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<TaskDto>> getAllTasks(@PathVariable @Valid String id){
        String correlationId = getCorrelationId();
        if(isUserAuthenticated()){
            logger.info("Correlation ID: {} - Getting all tasks for user ID {}",
                    correlationId, id);
            return ResponseEntity.ok(taskService.getAllTasksByUserId(id, getCurrentUser()));
        }
        else{
            logger.warn("Correlation ID: {} - User not authenticated. Cannot get the user {} tasks",
                    correlationId, id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @Operation(summary = "Update information for an existing task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task information updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Prohibited, perform authentication"),
            @ApiResponse(responseCode = "422", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Error updating task information"),
    })
    @PutMapping
    public ResponseEntity<TaskDto> update(@RequestBody @Valid TaskUpdateDto taskUpdateDto){
        String correlationId = getCorrelationId();
        if(isUserAuthenticated()){
            logger.info("Correlation ID: {} - Updating information at Task {} from user {}",
                    correlationId, taskUpdateDto.getTaskId(), taskUpdateDto.getUserId());
            return ResponseEntity.ok(taskService.updateTask(taskUpdateDto, getCurrentUser()));
        }
        else{
            logger.warn("Correlation ID: {} - User not authenticated. Cannot update the user {} task",
                    correlationId, taskUpdateDto.getUserId());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @Operation(summary = "Delete an existing task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Prohibited, perform authentication"),
            @ApiResponse(responseCode = "422", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Error deleting task"),
    })
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody @Valid TaskDeleteDto taskDeleteDto){
        String correlationId = getCorrelationId();
        if(isUserAuthenticated()){
            logger.info("Correlation ID: {} - Deleting Task with Id {} from user {}",
                    correlationId, taskDeleteDto.taskId(), taskDeleteDto.userId());
            taskService.deleteTask(taskDeleteDto, getCurrentUser());
        }
        else{
            logger.warn("Correlation ID: {} - User not authenticated. Cannot delete the user {} task",
                    correlationId, taskDeleteDto.userId());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok().build();
    }

    private boolean isUserAuthenticated(){
        Authentication authenticateUser = SecurityContextHolder.getContext().getAuthentication();
        return authenticateUser != null && authenticateUser.getPrincipal() instanceof UserDetails;
    }

    private User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private String getCorrelationId(){
        return UUID.randomUUID().toString();
    }
}
