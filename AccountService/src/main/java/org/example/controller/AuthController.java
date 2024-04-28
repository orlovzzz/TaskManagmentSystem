package org.example.controller;

import org.example.dto.AccountDTO;
import org.example.dto.ResponseDTO;
import org.example.entity.Account;
import org.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/checkAccount")
    public ResponseEntity<ResponseDTO> checkUserForLogin(@RequestBody AccountDTO account) {
        return new ResponseEntity<>(authService.checkUserForLogin(account), HttpStatus.OK);
    }

    @PostMapping("/createAccount")
    public ResponseEntity<ResponseDTO> checkUserForReg(@RequestBody AccountDTO account) {
        return new ResponseEntity<>(authService.checkUserForReg(account), HttpStatus.OK);
    }

}
