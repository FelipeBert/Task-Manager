package org.FelipeBert.TaskManager.service.impl;

import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskCreationDto;
import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskDeleteDto;
import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskDto;
import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskUpdateDto;
import org.FelipeBert.TaskManager.exceptions.*;
import org.FelipeBert.TaskManager.model.Task;
import org.FelipeBert.TaskManager.model.User;
import org.FelipeBert.TaskManager.model.enums.UserStatus;
import org.FelipeBert.TaskManager.repository.TaskRepository;
import org.FelipeBert.TaskManager.repository.UserRepository;
import org.FelipeBert.TaskManager.service.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LogManager.getLogger(TaskServiceImpl.class);
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TaskServiceImpl(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskDto create(TaskCreationDto creationDto, User user) {
        logger.info("Creating task for user {} with title {}", user.getId(), creationDto.getTitulo());

        if(user.getUserStatus() == UserStatus.INACTIVE){
            logger.warn("User {} is inactive. Cannot create task", user.getId());
            throw new AccountNotActivatedException();
        }
        if(!user.getId().equals(creationDto.getUserId())){
            logger.warn("User ID in creationDto {} does not match current user ID {}", creationDto.getUserId(), user.getId());
            throw new UserIdDoesNotMatchException();
        }

        User checkIfUserExist = userRepository.findById(creationDto.getUserId())
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found", creationDto.getUserId());
                    return new UserNotFoundException();
                });

        Task newTask = new Task();
        newTask.setTitulo(creationDto.getTitulo());
        newTask.setDescricao(creationDto.getDescricao());
        newTask.setCreationDate(LocalDateTime.now());
        newTask.setUpdateDate(LocalDateTime.now());
        newTask.setDueDate(creationDto.getDueDate());
        newTask.setTaskStatus(creationDto.getTaskStatus());
        newTask.setTaskPriority(creationDto.getTaskPriority());
        newTask.setTaskReferences(creationDto.getReferencesList());
        newTask.setUser(checkIfUserExist);

        taskRepository.save(newTask);
        checkIfUserExist.addOnList(newTask);

        logger.info("Task with title {} created successfully for user {}", newTask.getTitulo(), user.getId());

        return new TaskDto(newTask.getTitulo(), newTask.getDescricao(), newTask.getCreationDate(),
                newTask.getUpdateDate(), newTask.getDueDate(), newTask.getTaskStatus(), newTask.getTaskPriority(),
                newTask.getTaskReferences());
    }

    @Override
    public List<TaskDto> getAllTasksByUserId(String id, User user) {
        logger.info("Fetching all tasks for user ID {}", id);

        if(user.getUserStatus() == UserStatus.INACTIVE){
            logger.warn("User {} is inactive. Cannot fetch tasks", user.getId());
            throw new AccountNotActivatedException();
        }
        if(id == null){
            logger.warn("Provided user ID is null");
            throw new UserIdDoesNotMatchException();
        }
        if(!user.getId().equals(id)){
            logger.warn("User ID in request {} does not match current user ID {}", id, user.getId());
            throw new UserIdDoesNotMatchException();
        }

        List<Task> allTasks = taskRepository.findAllByUserId(id);
        List<TaskDto> taskDtoList = allTasks.stream().map(
                        task -> new TaskDto(task.getTitulo(),
                                task.getDescricao(),
                                task.getCreationDate(),
                                task.getUpdateDate(),
                                task.getDueDate(),
                                task.getTaskStatus(),
                                task.getTaskPriority(),
                                task.getTaskReferences()))
                .collect(Collectors.toList());

        logger.info("Fetched {} tasks for user ID {}", taskDtoList.size(), id);

        return taskDtoList;
    }

    @Override
    public TaskDto updateTask(TaskUpdateDto taskUpdateDto, User user) {
        logger.info("Updating task {} for user {}", taskUpdateDto.getTaskId(), user.getId());

        if(user.getUserStatus() == UserStatus.INACTIVE){
            logger.warn("User {} is inactive. Cannot update task", user.getId());
            throw new AccountNotActivatedException();
        }
        if(!user.getId().equals(taskUpdateDto.getUserId())){
            logger.warn("User ID in taskUpdateDto {} does not match current user ID {}", taskUpdateDto.getUserId(), user.getId());
            throw new UserIdDoesNotMatchException();
        }

        User checkIfUserExist = userRepository.findById(taskUpdateDto.getUserId())
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found", taskUpdateDto.getUserId());
                    return new UserNotFoundException();
                });

        Task task = taskRepository.findById(taskUpdateDto.getTaskId())
                .orElseThrow(() -> {
                    logger.error("Task with ID {} not found", taskUpdateDto.getTaskId());
                    return new TaskNotFoundException();
                });

        if(taskUpdateDto.getTitulo() != null && !taskUpdateDto.getTitulo().isEmpty()){
            task.setTitulo(taskUpdateDto.getTitulo());
        }
        if(taskUpdateDto.getDescricao() != null && !taskUpdateDto.getDescricao().isEmpty()){
            task.setDescricao(taskUpdateDto.getDescricao());
        }
        if(taskUpdateDto.getTaskStatus() != null){
            task.setTaskStatus(taskUpdateDto.getTaskStatus());
        }
        if(taskUpdateDto.getTaskPriority() != null){
            task.setTaskPriority(taskUpdateDto.getTaskPriority());
        }
        if(taskUpdateDto.getReferencesList() != null){
            task.setTaskReferences(taskUpdateDto.getReferencesList());
        }
        if(taskUpdateDto.getDueDate() != null){
            task.setDueDate(taskUpdateDto.getDueDate());
        }
        task.setUpdateDate(LocalDateTime.now());

        taskRepository.save(task);

        logger.info("Task {} updated successfully for user {}", task.getId(), user.getId());

        return new TaskDto(task.getTitulo(), task.getDescricao(), task.getCreationDate(), task.getUpdateDate(),
                task.getDueDate(), task.getTaskStatus(), task.getTaskPriority(), task.getTaskReferences());
    }

    @Override
    public void deleteTask(TaskDeleteDto taskDeleteDto, User user) {
        logger.info("Deleting task {} for user {}", taskDeleteDto.taskId(), user.getId());

        if(user.getUserStatus() == UserStatus.INACTIVE){
            logger.warn("User {} is inactive. Cannot delete task", user.getId());
            throw new AccountNotActivatedException();
        }
        if(!user.getId().equals(taskDeleteDto.userId())){
            logger.warn("User ID in taskDeleteDto {} does not match current user ID {}", taskDeleteDto.userId(), user.getId());
            throw new UserIdDoesNotMatchException();
        }

        User entity = userRepository.findById(taskDeleteDto.userId())
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found", taskDeleteDto.userId());
                    return new UserNotFoundException();
                });

        Task task = taskRepository.findById(taskDeleteDto.taskId())
                .orElseThrow(() -> {
                    logger.error("Task with ID {} not found", taskDeleteDto.taskId());
                    return new TaskNotFoundException();
                });

        if(!task.getUser().getId().equals(entity.getId())){
            logger.warn("Task {} does not belong to user {}", task.getId(), user.getId());
            throw new CannotDeleteTaskException();
        }

        taskRepository.delete(task);

        logger.info("Task {} deleted successfully for user {}", taskDeleteDto.taskId(), user.getId());
    }
}