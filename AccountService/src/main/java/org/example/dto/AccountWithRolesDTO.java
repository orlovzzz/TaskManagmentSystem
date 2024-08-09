package org.example.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AccountWithRolesDTO {
    private UUID id;
    private String email;
    private AuthorityDTO authority;
}
