package org.example.mapper;

import org.example.dto.AccountDTO;
import org.example.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {
    Account fromDto(AccountDTO account);
    AccountDTO fromEntity(Account account);
}

