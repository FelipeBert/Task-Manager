package org.FelipeBert.TaskManager.dtos.TaskDtos;

import lombok.Getter;
import org.FelipeBert.TaskManager.model.enums.TaskPriority;
import org.FelipeBert.TaskManager.model.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TaskDto {
    private String titulo;
    private String descricao;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private LocalDateTime dueDate;
    private TaskStatus taskStatus;
    private TaskPriority taskPriority;
    private List<String> referencesList;

    public TaskDto(String titulo, String descricao, LocalDateTime creationDate,
                   LocalDateTime updateDate, LocalDateTime dueDate, TaskStatus taskStatus,
                   TaskPriority taskPriority, List<String> referencesList) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.dueDate = dueDate;
        this.taskStatus = taskStatus;
        this.taskPriority = taskPriority;
        this.referencesList = referencesList;
    }
}
