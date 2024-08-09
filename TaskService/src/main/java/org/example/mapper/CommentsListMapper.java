package org.example.mapper;

import org.example.dto.CommentsResponseDTO;
import org.example.entity.Comments;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = CommentsMapper.class)
public interface CommentsListMapper {
    List<CommentsResponseDTO> fromEntity(List<Comments> comments);
}
