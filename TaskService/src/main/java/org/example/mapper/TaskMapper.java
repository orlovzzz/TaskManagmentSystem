package org.example.mapper;

import org.example.dto.TaskDTO;
import org.example.dto.TaskWithExecutorsDTO;
import org.example.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    Task fromDTO(TaskDTO taskDTO);
    TaskDTO fromEntity(Task task);
    TaskWithExecutorsDTO fromEntityForExecutorsTask(Task task);
}
