package org.example.mapper;

import org.example.dto.AccountDTO;
import org.example.dto.AccountWithRolesDTO;
import org.example.entity.Account;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = AuthorityMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AccountMapper {
    Account fromDto(AccountDTO account);
    AccountDTO fromEntity(Account account);
    AccountWithRolesDTO fromEntityToRolesDTO(Account account);
}

