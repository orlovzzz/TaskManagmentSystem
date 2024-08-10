package org.example.mapper;

import org.example.dto.TaskWithExecutorsDTO;
import org.example.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = TaskMapper.class)
public interface TaskListMapper {
    List<TaskWithExecutorsDTO> fromEntityToExecutorsTaskList(List<Task> tasks);
}
