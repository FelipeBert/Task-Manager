package com.fbert.TaskManager.entity.tarefa;

import com.fbert.TaskManager.dto.TaskDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "tarefa")
@Data
@Table(name = "tarefa")
@NoArgsConstructor
@AllArgsConstructor
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descricao;

    private LocalDateTime localDateTime;

    @Enumerated(EnumType.STRING)
    private TarefaStatus tarefaStatus;

    public Tarefa(TaskDTO tarefaDTO){
        this.descricao = tarefaDTO.getDescricao();
        this.titulo = tarefaDTO.getTitulo();
        this.tarefaStatus = tarefaDTO.getTarefaStatus();
        this.localDateTime = LocalDateTime.now();
    }

}
