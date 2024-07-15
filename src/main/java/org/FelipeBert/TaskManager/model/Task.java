package org.FelipeBert.TaskManager.model;

import jakarta.persistence.*;
import lombok.*;
import org.FelipeBert.TaskManager.model.enums.TaskPriority;
import org.FelipeBert.TaskManager.model.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private LocalDateTime updateDate;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskPriority taskPriority;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ElementCollection
    @CollectionTable(name = "task_references", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "reference")
    private List<String> taskReferences = new ArrayList<>();
}
