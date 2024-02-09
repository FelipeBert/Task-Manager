package com.fbert.TaskManager.repository;

import com.fbert.TaskManager.entity.tarefa.Tarefa;
import com.fbert.TaskManager.entity.tarefa.TarefaStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByTarefaStatus(TarefaStatus tarefaStatus);
}
