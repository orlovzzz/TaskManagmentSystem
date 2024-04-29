package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@Table(name = "executors_tasks")
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ExecutorsTasks implements Serializable {
    @EmbeddedId
    ExecutorsTasksId executorsTasksId;
}