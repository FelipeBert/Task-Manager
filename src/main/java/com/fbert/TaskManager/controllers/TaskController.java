package com.fbert.TaskManager.controllers;

import com.fbert.TaskManager.dto.TaskUpdateDTO;
import com.fbert.TaskManager.dto.TaskDTO;
import com.fbert.TaskManager.entity.tarefa.Tarefa;
import com.fbert.TaskManager.entity.tarefa.TarefaStatus;
import com.fbert.TaskManager.services.TarefasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefa")
public class TaskController {

    @Autowired
    private TarefasService tarefasService;

    @PostMapping
    public ResponseEntity<Tarefa> createTarefa(@RequestBody TaskDTO tarefaDTO){
        Tarefa tarefa = this.tarefasService.createTarefa(tarefaDTO);
        return new ResponseEntity<>(tarefa, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> getAllTarefas(){
        List<Tarefa> tarefas = this.tarefasService.getAllTarefas();
        return new ResponseEntity<>(tarefas, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<List<Tarefa>> getAllTarefasByStatus(@RequestParam TarefaStatus status){
        List<Tarefa> tarefas = this.tarefasService.getAllTarefasByStatus(status);
        return new ResponseEntity<>(tarefas, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTarefa(@RequestParam Long id){
        this.tarefasService.deleteTarefa(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Tarefa> atualizarTarefa(@RequestBody TaskUpdateDTO taskUpdateDTO)
                                                                                    throws Exception {
        Tarefa tarefa = this.tarefasService.atualizarTarefa(taskUpdateDTO);
        return new ResponseEntity<>(tarefa, HttpStatus.OK);
    }

}
