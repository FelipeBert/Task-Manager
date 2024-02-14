package com.fbert.TaskManager.services;

import com.fbert.TaskManager.dto.TaskUpdateDTO;
import com.fbert.TaskManager.dto.TaskDTO;
import com.fbert.TaskManager.entity.tarefa.Tarefa;
import com.fbert.TaskManager.entity.tarefa.TarefaStatus;
import com.fbert.TaskManager.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarefasService{

    @Autowired
    private TarefaRepository tarefaRepository;


    public Tarefa createTarefa(TaskDTO taskDTO){
        Tarefa tarefa = new Tarefa(taskDTO);
        this.tarefaRepository.save(tarefa);
        return tarefa;
    }

    public List<Tarefa> getAllTarefas(){
        return this.tarefaRepository.findAll();
    }
    public List<Tarefa> getAllTarefasByStatus(TarefaStatus status){
        return this.tarefaRepository.findByTarefaStatus(status);
    }

    public void deleteTarefa(Long id){
        this.tarefaRepository.deleteById(id);
    }

    public void deleteTask(Tarefa tarefa){
        this.tarefaRepository.delete(tarefa);
    }


    public Tarefa atualizarTarefa(TaskUpdateDTO taskUpdateDTO) throws Exception {
        Tarefa tarefaExistente = tarefaRepository.findById(taskUpdateDTO.getId())
                .orElseThrow(() -> new Exception("Tarefa não encontrada!"));

        if(taskUpdateDTO.getTitulo() != null){
            tarefaExistente.setTitulo(taskUpdateDTO.getTitulo());
        }
        if(taskUpdateDTO.getDescricao() != null){
            tarefaExistente.setDescricao(taskUpdateDTO.getDescricao());
        }
        if(taskUpdateDTO.getTarefaStatus() != null){
            tarefaExistente.setTarefaStatus(TarefaStatus.valueOf(String.valueOf(taskUpdateDTO.getTarefaStatus())));
        }
        this.tarefaRepository.save(tarefaExistente);
        return tarefaExistente;
    }
}
