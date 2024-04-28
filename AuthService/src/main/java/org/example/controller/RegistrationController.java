package org.example.controller;

import org.example.dto.AccountDTO;
import org.example.dto.ResponseDTO;
import org.example.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/registration")
    public ResponseEntity<ResponseDTO> registration(@RequestBody AccountDTO account) {
        ResponseDTO response = registrationService.registration(account);
        if (response.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}