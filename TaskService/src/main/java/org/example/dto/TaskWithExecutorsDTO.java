package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.enums.Priority;
import org.example.enums.Status;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class TaskWithExecutorsDTO {
    protected UUID id;
    protected String title;
    protected String description;
    protected Priority priority;
    protected Status status;
    protected UUID authorId;
    protected List<CommentsResponseDTO> comments;
    private Set<AccountDTO> executors;
}
