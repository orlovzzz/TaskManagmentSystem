package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.Priority;
import org.example.enums.Status;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    protected UUID id;
    protected String title;
    protected String description;
    protected Priority priority;
    protected Status status;
    protected UUID authorId;
    private int executorsCount;
}