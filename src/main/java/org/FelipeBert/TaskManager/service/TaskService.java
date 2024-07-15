package org.FelipeBert.TaskManager.service;

import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskCreationDto;
import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskDeleteDto;
import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskDto;
import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskUpdateDto;
import org.FelipeBert.TaskManager.model.User;

import java.util.List;

public interface TaskService {
    public TaskDto create(TaskCreationDto creationDto, User user);
    public List<TaskDto> getAllTasksByUserId(String id, User user);
    public TaskDto updateTask(TaskUpdateDto taskUpdateDto, User user);
    public void deleteTask(TaskDeleteDto taskDeleteDto, User user);
}
