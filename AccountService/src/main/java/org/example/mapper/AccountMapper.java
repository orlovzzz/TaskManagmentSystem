package org.example.mapper;

import org.example.dto.AccountDTO;
import org.example.dto.AccountWithRolesDTO;
import org.example.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = AuthorityMapper.class)
public interface AccountMapper {
    Account fromDto(AccountDTO account);
    AccountDTO fromEntity(Account account);
    AccountWithRolesDTO fromEntityToRolesDTO(Account account);
}

