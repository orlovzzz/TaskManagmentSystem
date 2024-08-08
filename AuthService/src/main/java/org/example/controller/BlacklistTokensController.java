package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.example.service.BlacklistTokensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Blacklist Token Controller", description = "Add token in blacklist and check is exist")
@Controller
@CrossOrigin
public class BlacklistTokensController {

    @Autowired
    private BlacklistTokensService blacklistTokensService;

    @Operation(summary = "Add token in blacklist")
    @PostMapping("/authApi/blacklist")
    public ResponseEntity addTokenInBlacklist(String token) {
        blacklistTokensService.addTokenInBlacklist(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Check token in blacklist")
    @GetMapping("/blacklist")
    public ResponseEntity<Boolean> isTokenInBlacklist(@RequestParam("token") String token) {
        return new ResponseEntity<>(blacklistTokensService.isTokenInBlacklist(token), HttpStatus.OK);
    }

}
