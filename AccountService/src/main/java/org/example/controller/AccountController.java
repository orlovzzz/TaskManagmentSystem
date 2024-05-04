package org.example.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.AccountDTO;
import org.example.dto.AccountWithRolesDTO;
import org.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/myAccount")
    @RolesAllowed({ "USER", "EXECUTOR", "ADMIN" })
    public ResponseEntity<AccountWithRolesDTO> getMyAccount(HttpServletRequest request) {
        try {
            return new ResponseEntity<>(accountService.getAccountById(UUID.fromString(
                    getTokenFromHeader(request).substring("Bearer ".length()))), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/accounts")
    @RolesAllowed({ "USER", "EXECUTOR", "ADMIN" })
    public ResponseEntity<List<AccountWithRolesDTO>> getAllAccounts(HttpServletRequest request) {
        return new ResponseEntity<>(accountService.getAllAccounts(getTokenFromHeader(request)), HttpStatus.OK);
    }

    @GetMapping("/accounts/{id}")
    @RolesAllowed({ "USER", "EXECUTOR", "ADMIN" })
    public ResponseEntity<AccountWithRolesDTO> getAccountById(@PathVariable String id) {
        try {
            return new ResponseEntity<>(accountService.getAccountById(UUID.fromString(id)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/accounts")
    @RolesAllowed({ "USER" })
    public ResponseEntity<String> setAccountToExecutorRole(HttpServletRequest request) {
        try {
            accountService.setAccountToExecutorRole(getTokenFromHeader(request));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

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

    private static String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader("Authorization").substring("Bearer ".length());
    }
}
