package org.example.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.AccountDTO;
import org.example.dto.AccountWithRolesDTO;
import org.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Tag(name = "Account Controller", description = "Get, delete, change accounts methods")
@RestController
@RequestMapping("/accountApi")
@CrossOrigin
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(summary = "Get current user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = AccountWithRolesDTO.class)),
            }),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/myAccount")
    @RolesAllowed({ "USER", "EXECUTOR", "ADMIN" })
    public ResponseEntity<AccountWithRolesDTO> getMyAccount(HttpServletRequest request) {
        try {
            return new ResponseEntity<>(accountService.getMyAccount(getTokenFromHeader(request)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AccountWithRolesDTO.class)))
            })
    })
    @GetMapping("/accounts")
    @RolesAllowed({ "USER", "EXECUTOR", "ADMIN" })
    public ResponseEntity<List<AccountWithRolesDTO>> getAllAccounts(HttpServletRequest request) {
        return new ResponseEntity<>(accountService.getAllAccounts(getTokenFromHeader(request)), HttpStatus.OK);
    }

    @Operation(summary = "Get account by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AccountWithRolesDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/accounts/{id}")
    @RolesAllowed({ "USER", "EXECUTOR", "ADMIN" })
    @CrossOrigin
    public ResponseEntity<AccountWithRolesDTO> getAccountById(@PathVariable String id) {
        try {
            return new ResponseEntity<>(accountService.getAccountById(UUID.fromString(id)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Set account to executor role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PostMapping("/accounts")
    @RolesAllowed({ "USER" })
    public ResponseEntity setAccountToExecutorRole(HttpServletRequest request) {
        try {
            accountService.setAccountToExecutorRole(getTokenFromHeader(request));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @DeleteMapping("/accounts")
    @RolesAllowed({ "ADMIN" })
    public ResponseEntity deleteAccount(@RequestParam("id") String id) {
        try {
            accountService.deleteAccount(UUID.fromString(id));
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Change account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PutMapping("/accounts")
    @RolesAllowed({ "ADMIN" })
    public ResponseEntity changeAccount(@RequestBody AccountDTO accountDTO) {
        try {
            accountService.changeAccount(accountDTO);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader("Authorization").substring("Bearer ".length());
    }
}
