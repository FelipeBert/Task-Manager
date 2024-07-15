package org.FelipeBert.TaskManager.dtos.TaskDtos;

import lombok.Getter;
import org.FelipeBert.TaskManager.model.enums.TaskPriority;
import org.FelipeBert.TaskManager.model.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TaskUpdateDto {
    private String titulo;
    private String descricao;
    private String userId;
    private String taskId;
    private TaskStatus taskStatus;
    private TaskPriority taskPriority;
    private List<String> referencesList;
    private LocalDateTime dueDate;

    public TaskUpdateDto(String titulo, String descricao, String userId, String taskId,TaskStatus taskStatus,
                         TaskPriority taskPriority, List<String> referencesList, LocalDateTime dueDate) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.userId = userId;
        this.taskId = taskId;
        this.taskStatus = taskStatus;
        this.taskPriority = taskPriority;
        this.referencesList = referencesList;
        this.dueDate = dueDate;
    }
}
