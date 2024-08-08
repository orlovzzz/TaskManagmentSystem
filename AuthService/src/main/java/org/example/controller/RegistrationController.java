package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.AccountDTO;
import org.example.dto.ResponseDTO;
import org.example.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Registration Controller", description = "Account registration")
@RestController
@RequestMapping("/authApi")
@CrossOrigin(maxAge = 3600000)
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Operation(summary = "Registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Account already been registered", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseDTO.class))
            })
    })
    @PostMapping("/registration")
    public ResponseEntity<ResponseDTO> registration(@RequestBody AccountDTO account) {
        ResponseDTO response = registrationService.registration(account);
        if (response.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}