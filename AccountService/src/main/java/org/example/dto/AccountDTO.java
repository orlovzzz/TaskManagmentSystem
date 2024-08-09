package org.example.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
public class AccountDTO {
    protected UUID id;
    protected String email;
    private String password;
}
