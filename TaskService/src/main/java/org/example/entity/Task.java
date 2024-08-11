package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.Priority;
import org.example.enums.Status;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name = "author_id")
    private UUID authorId;

    @OneToMany(mappedBy = "executorsTasksId.task", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ExecutorsTasks> executorsTasks = new ArrayList<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Comments> comments = new ArrayList<>();

    public Task(String title, String description, Status status, Priority priority) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.authorId = UUID.randomUUID();
    }

    public Task(UUID id, String title, String description, Status status, Priority priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.authorId = UUID.randomUUID();
    }

    public Task(UUID id, String title, String description, Status status, Priority priority, UUID authorId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.authorId = authorId;
    }

    public void addToExecutorsTasks(ExecutorsTasks executorsTasks) {
        this.executorsTasks.add(executorsTasks);
    }

}