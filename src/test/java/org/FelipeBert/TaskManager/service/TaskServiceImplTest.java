package org.FelipeBert.TaskManager.service;

import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskCreationDto;
import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskDto;
import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskUpdateDto;
import org.FelipeBert.TaskManager.dtos.TaskDtos.TaskDeleteDto;
import org.FelipeBert.TaskManager.exceptions.UserIdDoesNotMatchException;
import org.FelipeBert.TaskManager.model.Task;
import org.FelipeBert.TaskManager.model.User;
import org.FelipeBert.TaskManager.model.enums.TaskPriority;
import org.FelipeBert.TaskManager.model.enums.TaskStatus;
import org.FelipeBert.TaskManager.repository.TaskRepository;
import org.FelipeBert.TaskManager.repository.UserRepository;
import org.FelipeBert.TaskManager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TaskServiceImplTest {
    @Test
    public void test_create_task_with_valid_details() {
        UserRepository userRepository = mock(UserRepository.class);
        TaskRepository taskRepository = mock(TaskRepository.class);
        TaskServiceImpl taskService = new TaskServiceImpl(userRepository, taskRepository);

        User user = new User();
        user.setId("user123");

        TaskCreationDto creationDto = new TaskCreationDto("Task Title", "Task Description", LocalDateTime.now().plusDays(1),
                TaskStatus.PENDING, TaskPriority.HIGH, List.of("ref1", "ref2"), "user123");

        when(userRepository.findById("user123")).thenReturn(Optional.of(user));

        TaskDto result = taskService.create(creationDto, user);

        assertNotNull(result);
        assertEquals("Task Title", result.getTitulo());
        assertEquals("Task Description", result.getDescricao());
        assertEquals(TaskStatus.PENDING, result.getTaskStatus());
        assertEquals(TaskPriority.HIGH, result.getTaskPriority());
    }

    @Test
    public void test_create_task_user_id_mismatch() {
        UserRepository userRepository = mock(UserRepository.class);
        TaskRepository taskRepository = mock(TaskRepository.class);
        TaskServiceImpl taskService = new TaskServiceImpl(userRepository, taskRepository);

        User user = new User();
        user.setId("user123");

        TaskCreationDto creationDto = new TaskCreationDto("Task Title", "Task Description", LocalDateTime.now().plusDays(1),
                TaskStatus.PENDING, TaskPriority.HIGH, List.of("ref1", "ref2"), "user456");

        assertThrows(UserIdDoesNotMatchException.class, () -> {
            taskService.create(creationDto, user);
        });
    }

    @Test
    public void retrieves_all_tasks_for_valid_user_id() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
        TaskServiceImpl taskService = new TaskServiceImpl(userRepository, taskRepository);

        User user = new User();
        user.setId("validUserId");

        List<Task> tasks = List.of(
                new Task("1", "Task 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(1), TaskStatus.PENDING, TaskPriority.HIGH, user, List.of("Ref1")),
                new Task("2", "Task 2", "Description 2", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(2), TaskStatus.COMPLETED, TaskPriority.LOW, user, List.of("Ref2"))
        );

        Mockito.when(taskRepository.findAllByUserId("validUserId")).thenReturn(tasks);

        List<TaskDto> result = taskService.getAllTasksByUserId("validUserId", user);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Task 1", result.get(0).getTitulo());
        Assertions.assertEquals("Task 2", result.get(1).getTitulo());
    }

    @Test
    public void user_id_is_null() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
        TaskServiceImpl taskService = new TaskServiceImpl(userRepository, taskRepository);

        User user = new User();
        user.setId(null);

        Assertions.assertThrows(UserIdDoesNotMatchException.class, () -> {
            taskService.getAllTasksByUserId(null, user);
        });
    }

    @Test
    public void test_successful_task_update() {
        UserRepository userRepository = mock(UserRepository.class);
        TaskRepository taskRepository = mock(TaskRepository.class);
        TaskServiceImpl taskService = new TaskServiceImpl(userRepository, taskRepository);

        User user = new User();
        user.setId("user123");

        TaskUpdateDto taskUpdateDto = new TaskUpdateDto("New Title", "New Description", "user123", "task123",
                TaskStatus.IN_PROGRESS, TaskPriority.HIGH,
                List.of("ref1", "ref2"), LocalDateTime.now().plusDays(1));

        Task task = new Task();
        task.setId("task123");
        task.setTitulo("Old Title");
        task.setDescricao("Old Description");
        task.setCreationDate(LocalDateTime.now().minusDays(1));
        task.setUpdateDate(LocalDateTime.now().minusDays(1));
        task.setDueDate(LocalDateTime.now().plusDays(2));
        task.setTaskStatus(TaskStatus.PENDING);
        task.setTaskPriority(TaskPriority.LOW);
        task.setTaskReferences(List.of("oldRef"));
        task.setUser(user);

        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(taskRepository.findById("task123")).thenReturn(Optional.of(task));

        TaskDto updatedTask = taskService.updateTask(taskUpdateDto, user);

        assertEquals("New Title", updatedTask.getTitulo());
        assertEquals("New Description", updatedTask.getDescricao());
        assertEquals(TaskStatus.IN_PROGRESS, updatedTask.getTaskStatus());
        assertEquals(TaskPriority.HIGH, updatedTask.getTaskPriority());
        assertEquals(List.of("ref1", "ref2"), updatedTask.getReferencesList());
    }

    @Test
    public void test_user_id_does_not_match_exception() {
        UserRepository userRepository = mock(UserRepository.class);
        TaskRepository taskRepository = mock(TaskRepository.class);
        TaskServiceImpl taskService = new TaskServiceImpl(userRepository, taskRepository);

        User user = new User();
        user.setId("user123");

        TaskUpdateDto taskUpdateDto = new TaskUpdateDto("New Title", "New Description", "user456", "task123",
                TaskStatus.IN_PROGRESS, TaskPriority.HIGH,
                List.of("ref1", "ref2"), LocalDateTime.now().plusDays(1));

        assertThrows(UserIdDoesNotMatchException.class, () -> {
            taskService.updateTask(taskUpdateDto, user);
        });
    }

    @Test
    public void test_delete_task_success() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
        TaskServiceImpl taskService = new TaskServiceImpl(userRepository, taskRepository);

        String userId = "user123";
        String taskId = "task123";
        User user = new User();
        user.setId(userId);
        Task task = new Task();
        task.setId(taskId);
        task.setUser(user);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        TaskDeleteDto taskDeleteDto = new TaskDeleteDto(userId, taskId);

        taskService.deleteTask(taskDeleteDto, user);

        Mockito.verify(taskRepository, Mockito.times(1)).delete(task);
    }

    @Test
    public void test_delete_task_user_id_does_not_match() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
        TaskServiceImpl taskService = new TaskServiceImpl(userRepository, taskRepository);

        String userId = "user123";
        String differentUserId = "user456";
        String taskId = "task123";
        User user = new User();
        user.setId(differentUserId);

        TaskDeleteDto taskDeleteDto = new TaskDeleteDto(userId, taskId);

        Assertions.assertThrows(UserIdDoesNotMatchException.class, () -> {
            taskService.deleteTask(taskDeleteDto, user);
        });
    }
}
