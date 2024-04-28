package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "executors_task")
public class ExecutorsTasks {
    private UUID executorId;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}