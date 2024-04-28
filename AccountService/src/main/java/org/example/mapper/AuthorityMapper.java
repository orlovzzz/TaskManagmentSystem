package org.example.mapper;

import org.example.dto.AuthorityDTO;
import org.example.entity.Authority;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthorityMapper {
    AuthorityDTO fromEntity(Authority authority);
}
