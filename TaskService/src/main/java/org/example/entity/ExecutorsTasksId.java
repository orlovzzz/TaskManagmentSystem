package org.example.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExecutorsTasksId {
    @JoinColumn(name = "executor_id")
    private UUID executorId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
