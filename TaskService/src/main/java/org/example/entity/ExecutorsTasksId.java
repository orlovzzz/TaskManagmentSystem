package org.example.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExecutorsTasksId {
    @JoinColumn(name = "executor_id")
    private UUID executorId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}