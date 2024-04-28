package org.example.dto;

import lombok.Data;
import org.example.enums.Roles;

@Data
public class AuthorityDTO {
    private Roles role;
}
