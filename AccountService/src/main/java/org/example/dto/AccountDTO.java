package org.example.dto;

import lombok.*;

import java.util.UUID;

@Data
public class AccountDTO {
    private UUID id;
    private String email;
    private String password;
}
