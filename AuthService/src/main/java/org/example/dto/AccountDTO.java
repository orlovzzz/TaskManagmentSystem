package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AccountDTO {
    private UUID id;
    private String email;
    private String password;
}
