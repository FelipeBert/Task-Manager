package com.fbert.TaskManager.dto;

import com.fbert.TaskManager.entity.tarefa.TarefaStatus;
import lombok.Data;

@Data
public class TaskUpdateDTO {
    private Long id;
    private String descricao;
    private String titulo;
    private TarefaStatus tarefaStatus;

    public TaskUpdateDTO(Long id, String titulo, String descricao, String tarefaStatus){
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tarefaStatus = TarefaStatus.valueOf(tarefaStatus);
    }
}
