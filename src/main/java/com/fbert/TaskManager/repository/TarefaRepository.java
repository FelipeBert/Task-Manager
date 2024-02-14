package com.fbert.TaskManager.repository;

import com.fbert.TaskManager.entity.tarefa.Tarefa;
import com.fbert.TaskManager.entity.tarefa.TarefaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    Tarefa findByTitulo(String titulo);
    List<Tarefa> findByTarefaStatus(TarefaStatus tarefaStatus);

    Optional<Tarefa> findByTituloEquals(String titulo);
}
