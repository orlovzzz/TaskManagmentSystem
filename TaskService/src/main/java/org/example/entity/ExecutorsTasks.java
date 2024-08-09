package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "executors_tasks")
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExecutorsTasks implements Serializable {
    @EmbeddedId
    ExecutorsTasksId executorsTasksId;
}