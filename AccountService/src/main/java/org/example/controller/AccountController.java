package org.example.controller;

import jakarta.annotation.security.RolesAllowed;
import org.example.dto.AccountDTO;
import org.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts")
//    @RolesAllowed({ "USER", "EXECUTOR", "ADMIN" })
    public ResponseEntity<List<AccountDTO>> getAllAccounts(@RequestParam("token") String token) {
        accountService.getAllAccounts(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
