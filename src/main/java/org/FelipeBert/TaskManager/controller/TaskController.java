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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
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
        if(isUserAuthenticated()){
            return ResponseEntity.ok(taskService.create(taskCreationDto, getCurrentUser()));
        }
        else{
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
    public ResponseEntity<List<TaskDto>> getAllTasks(@PathVariable String id){
        if(isUserAuthenticated()){
            return ResponseEntity.ok(taskService.getAllTasksByUserId(id, getCurrentUser()));
        }
        else{
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
    public ResponseEntity<TaskDto> update(@RequestBody TaskUpdateDto taskUpdateDto){
        if(isUserAuthenticated()){
            return ResponseEntity.ok(taskService.updateTask(taskUpdateDto, getCurrentUser()));
        }
        else{
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
    public ResponseEntity<Void> delete(@RequestBody TaskDeleteDto taskDeleteDto){
        if(isUserAuthenticated()){
            taskService.deleteTask(taskDeleteDto, getCurrentUser());
        }
        return null;
    }

    private boolean isUserAuthenticated(){
        Authentication authenticateUser = SecurityContextHolder.getContext().getAuthentication();
        return authenticateUser != null && authenticateUser.getPrincipal() instanceof UserDetails;
    }

    private User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
