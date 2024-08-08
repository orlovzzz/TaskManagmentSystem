package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.AccountDTO;
import org.example.dto.ResponseDTO;
import org.example.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Login Controller", description = "Check is correct account data")
@RestController
@RequestMapping("/authApi")
@CrossOrigin
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Operation(summary = "Send message to account service for checking is account data correct")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Wrong email or password")
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody AccountDTO account) {
        ResponseDTO response = loginService.login(account);
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}