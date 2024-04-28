package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountWithRoleDTO {
    private String email;
    private String password;
    private AuthorityDTO authority;
}
