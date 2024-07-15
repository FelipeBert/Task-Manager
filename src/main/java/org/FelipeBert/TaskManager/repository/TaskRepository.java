package org.FelipeBert.TaskManager.repository;

import org.FelipeBert.TaskManager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String> {
    Optional<Task> findById(String id);
    List<Task> findAllByUserId(String id);
}
