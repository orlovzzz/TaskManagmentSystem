package org.example.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.Account;
import org.example.service.AccountService;
import org.example.views.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@Tag(name = "Account Controller", description = "Registration, authorization account and show all accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @JsonView(Views.ViewForAccounts.class)
    @GetMapping("")
    @Operation(summary = "Get all accounts")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Account.class)))
    public ResponseEntity getAllAccounts() {
        return new ResponseEntity(accountService.getAllAccounts(), HttpStatus.OK);
    }

    @Operation(summary = "Registration user")
    @PostMapping("/registration")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Account created"), @ApiResponse(responseCode = "400")})
    public ResponseEntity createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @PostMapping("/login")
    @Operation(summary = "Authorization user")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Account not found"), @ApiResponse(responseCode = "400", description = "Password incorrect"), @ApiResponse(responseCode = "200")})
    public ResponseEntity login(HttpServletResponse response, @RequestBody Account account) {
        ResponseEntity entity = accountService.login(account);
        if (entity.getStatusCode() == HttpStatus.ACCEPTED) {
            Cookie cookie = new Cookie("jwt", (String) entity.getBody());
            cookie.setPath("/");
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
        }
        return entity;
    }

}
