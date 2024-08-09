package org.example.mapper;

import org.example.dto.CommentsResponseDTO;
import org.example.entity.Comments;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentsMapper {
    CommentsResponseDTO fromEntity(Comments comments);
}
