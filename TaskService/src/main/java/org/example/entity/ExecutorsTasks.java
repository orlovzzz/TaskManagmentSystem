package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@Table(name = "executors_tasks")
@Embeddable
public class ExecutorsTasks implements Serializable {
    @EmbeddedId
    ExecutorsTasksId executorsTasksId;
}