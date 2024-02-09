package com.fbert.TaskManager.dto;

import com.fbert.TaskManager.entity.tarefa.TarefaStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskDTO {

    @NotBlank(message = "Field cannot be empty or null")
    private String titulo;
    private String descricao;
    @NotNull(message = "Field cannot be empty")
    private TarefaStatus tarefaStatus;

    public TaskDTO(String titulo, String descricao, TarefaStatus tarefaStatus){
        this.descricao = descricao;
        this.tarefaStatus = tarefaStatus;
        this.titulo = titulo;
    }
}
