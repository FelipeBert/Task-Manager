package com.fbert.TaskManager.services;

import com.fbert.TaskManager.dto.TaskDTO;
import com.fbert.TaskManager.dto.TaskUpdateDTO;
import com.fbert.TaskManager.entity.tarefa.Tarefa;
import com.fbert.TaskManager.repository.TarefaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TarefasServiceTest {

    @Autowired
    TarefasService tarefasService;

    @Autowired
    TarefaRepository tarefaRepository;

    @Test
    @DisplayName("Should Create a new Task")
    void testCreateTarefaCase1() {
            TaskDTO data = new TaskDTO("Creation", "testing creation of task", "PENDENTE");
            Tarefa createdTarefa = tarefasService.createTarefa(data);

            Optional<Tarefa> findTarefa = tarefaRepository.findByTituloEquals("Creation");

        assertNotNull(createdTarefa);
        assertEquals(data.getTarefaStatus(), createdTarefa.getTarefaStatus());
        assertEquals(data.getTitulo(), createdTarefa.getTitulo());
        assertEquals(data.getDescricao(), createdTarefa.getDescricao());
        assertNotNull(findTarefa);
    }

    @Test
    @DisplayName("Should not Create a new Task")
    void testCreateTarefaCase2() {
        TaskDTO data = new TaskDTO(null, "dont create task", "PENDENTE");

        assertThrows(DataIntegrityViolationException.class, () -> tarefasService.createTarefa(data));
    }

    @Test
    @DisplayName("Should delete the task")
    void testDeleteTaskCase1(){
        TaskDTO data = new TaskDTO("Delete", "testing creation of task", "PENDENTE");
        Tarefa createdTarefa = tarefasService.createTarefa(data);

        Tarefa findTask = tarefaRepository.findByTitulo("Delete");

        tarefasService.deleteTask(findTask);
        assertNotNull(findTask);
    }

    @Test
    @DisplayName("Should update the task")
    void testUpdateTaskCase1() throws Exception {
        TaskDTO data = new TaskDTO("Update", "testing creation of task", "PROSSEGUIMENTO");
        Tarefa createdTask = tarefasService.createTarefa(data);

        Tarefa getIdTarefa = tarefaRepository.findByTitulo("Update");
        Long idTarefa = getIdTarefa.getId();

        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO(idTarefa, "Update test",
                "Creation of update test", "CONCLUIDA");

        Tarefa updatedTask = tarefasService.atualizarTarefa(taskUpdateDTO);
        Optional<Tarefa> findTarefa = tarefaRepository.findById(idTarefa);

        assertEquals(updatedTask.getDescricao(), taskUpdateDTO.getDescricao());
        assertEquals(updatedTask.getTarefaStatus(), taskUpdateDTO.getTarefaStatus());
        assertEquals(updatedTask.getTitulo(), taskUpdateDTO.getTitulo());
        assertTrue(findTarefa.isPresent());
    }
}