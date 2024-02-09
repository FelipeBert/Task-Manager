package com.fbert.TaskManager.dto;

import com.fbert.TaskManager.entity.tarefa.TarefaStatus;
import lombok.Data;

@Data
public class TaskUpdateDTO {
    private Long id;
    private String descricao;
    private String titulo;
    private String tarefaStatus;

    public TaskUpdateDTO(Long id, String descricao, String titulo, TarefaStatus tarefaStatus){
        this.id = id;
        this.descricao = descricao;
        this.titulo = titulo;
        this.tarefaStatus = String.valueOf(tarefaStatus);
    }
}
