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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TaskServiceImpl(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskDto create(TaskCreationDto creationDto, User user) {
        if(user.getUserStatus() == UserStatus.INACTIVE){
            throw new AccountNotActivatedException();
        }
        if(!user.getId().equals(creationDto.getUserId())){
            throw new UserIdDoesNotMatchException();
        }
        User checkIfUserExist = userRepository.findById(creationDto.getUserId())
                .orElseThrow(UserNotFoundException::new);

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

        return new TaskDto(creationDto.getTitulo(), creationDto.getDescricao(), creationDto.getCreationDate(),
                creationDto.getUpdateDate(), creationDto.getDueDate(), creationDto.getTaskStatus(), creationDto.getTaskPriority(),
                creationDto.getReferencesList());
    }

    @Override
    public List<TaskDto> getAllTasksByUserId(String id, User user) {
        if(user.getUserStatus() == UserStatus.INACTIVE){
            throw new AccountNotActivatedException();
        }
        if(id == null){
            throw new UserIdDoesNotMatchException();
        }
        if(!user.getId().equals(id)){
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
        return taskDtoList;
    }

    @Override
    public TaskDto updateTask(TaskUpdateDto taskUpdateDto, User user) {
        if(user.getUserStatus() == UserStatus.INACTIVE){
            throw new AccountNotActivatedException();
        }
        if(!user.getId().equals(taskUpdateDto.getUserId())){
            throw new UserIdDoesNotMatchException();
        }
        User checkIfUserExist = userRepository.findById(taskUpdateDto.getUserId())
                .orElseThrow(UserNotFoundException::new);

        Task task = taskRepository.findById(taskUpdateDto.getTaskId())
                .orElseThrow(TaskNotFoundException::new);

        if(taskUpdateDto.getTitulo() != null || !taskUpdateDto.getTitulo().isEmpty()){
            task.setTitulo(taskUpdateDto.getTitulo());
        }
        if(taskUpdateDto.getDescricao() != null || !taskUpdateDto.getDescricao().isEmpty()){
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

        return new TaskDto(task.getTitulo(), task.getDescricao(), task.getCreationDate(), task.getUpdateDate(),
                task.getDueDate(), task.getTaskStatus(), task.getTaskPriority(), task.getTaskReferences());
    }

    @Override
    public void deleteTask(TaskDeleteDto taskDeleteDto, User user) {
        if(user.getUserStatus() == UserStatus.INACTIVE){
            throw new AccountNotActivatedException();
        }
        if(!user.getId().equals(taskDeleteDto.userId())){
            throw new UserIdDoesNotMatchException();
        }
        User entity = userRepository.findById(taskDeleteDto.userId()).orElseThrow(UserNotFoundException::new);
        Task task = taskRepository.findById(taskDeleteDto.taskId()).orElseThrow(TaskNotFoundException::new);

        if(!task.getUser().getId().equals(entity.getId())){
            throw new CannotDeleteTaskException();
        }

        taskRepository.delete(task);
    }
}
