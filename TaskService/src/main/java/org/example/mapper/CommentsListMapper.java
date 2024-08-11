package org.example.mapper;

import org.example.dto.CommentsResponseDTO;
import org.example.entity.Comments;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = CommentsMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentsListMapper {
    List<CommentsResponseDTO> fromEntity(List<Comments> comments);
}
