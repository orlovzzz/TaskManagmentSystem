package org.example.dto;

import lombok.Data;
import org.example.enums.Priority;
import org.example.enums.Status;

import java.util.UUID;

@Data
public class TaskDTO {
    private UUID id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private UUID authorId;
    private int executorsCount;
}