package org.example.dto;

import lombok.Data;

@Data
public class AccountWithRoleDTO {
    private String email;
    private String password;
    private AuthorityDTO authorityDTO;
}
