package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.AccountDTO;
import org.example.dto.ResponseDTO;
import org.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth Controller", description = "Controller for auth service")
@RequestMapping("/accountApi")
@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @Operation(summary = "Checking is account data correct")
    @PostMapping("/checkAccount")
    public ResponseEntity<ResponseDTO> checkUserForLogin(@RequestBody AccountDTO account) {
        return new ResponseEntity<>(authService.checkUserForLogin(account), HttpStatus.OK);
    }

    @Operation(summary = "Create account")
    @PostMapping("/createAccount")
    public ResponseEntity<ResponseDTO> checkUserForReg(@RequestBody AccountDTO account) {
        return new ResponseEntity<>(authService.checkUserForReg(account), HttpStatus.OK);
    }

}
