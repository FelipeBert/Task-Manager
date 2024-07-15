package org.FelipeBert.TaskManager.dtos.TaskDtos;

import lombok.Getter;
import org.FelipeBert.TaskManager.model.enums.TaskPriority;
import org.FelipeBert.TaskManager.model.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TaskCreationDto {
    private String titulo;
    private String descricao;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private LocalDateTime dueDate;
    private TaskStatus taskStatus;
    private TaskPriority taskPriority;
    private List<String> referencesList;
    private String userId;

    public TaskCreationDto(String titulo, String descricao, LocalDateTime dueDate, TaskStatus taskStatus,
                           TaskPriority taskPriority, List<String> referencesList, String userId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.creationDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
        this.dueDate = dueDate;
        this.taskStatus = taskStatus;
        this.taskPriority = taskPriority;
        this.referencesList = referencesList;
        this.userId = userId;
    }
}
