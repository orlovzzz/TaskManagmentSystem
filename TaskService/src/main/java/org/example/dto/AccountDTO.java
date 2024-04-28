package org.example.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AccountDTO {
    private UUID id;
    private String email;
}