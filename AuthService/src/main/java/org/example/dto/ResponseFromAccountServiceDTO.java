package org.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseFromAccountServiceDTO {
    private boolean isSuccess;
    private AccountDTO account;
}
