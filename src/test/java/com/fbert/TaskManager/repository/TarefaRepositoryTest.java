package com.fbert.TaskManager.repository;

import com.fbert.TaskManager.dto.TaskDTO;
import com.fbert.TaskManager.entity.tarefa.Tarefa;
import com.fbert.TaskManager.entity.tarefa.TarefaStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class TarefaRepositoryTest {
    @Autowired
    TarefaRepository tarefaRepository;

    @Test
    @DisplayName("Should return task Status")
    void findByTarefaStatusSucess() {
        String tarefaStatus = "PENDENTE";
        TaskDTO data = new TaskDTO("New Task", "Task test sucess", tarefaStatus);
        this.createTarefa(data);

        List<Tarefa> foundedTarefa = this.tarefaRepository.findByTarefaStatus(TarefaStatus.valueOf(tarefaStatus));
        assertThat(foundedTarefa.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Should not return task Status")
    void findByTarefaStatusFailure() {
        String tarefaStatus = "CONCLUIDA";

        List<Tarefa> foundedTarefa = this.tarefaRepository.findByTarefaStatus(TarefaStatus.valueOf(tarefaStatus));
        assertThat(foundedTarefa.isEmpty()).isTrue();
    }

    private Tarefa createTarefa(TaskDTO taskDTO){
        Tarefa newTarefa = new Tarefa(taskDTO);
        tarefaRepository.save(newTarefa);
        return newTarefa;
    }
}