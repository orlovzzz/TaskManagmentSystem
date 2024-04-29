package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class TaskWithExecutorsDTO extends TaskDTO {

    private Set<AccountDTO> executors;

}
