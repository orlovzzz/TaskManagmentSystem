package org.example.dto;

import lombok.Data;

@Data
public class AccountWithRolesDTO extends AccountDTO{
    private AuthorityDTO authority;
}
