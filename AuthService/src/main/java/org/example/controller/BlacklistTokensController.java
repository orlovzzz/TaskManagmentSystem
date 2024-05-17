package org.example.controller;

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

@Controller
@CrossOrigin
public class BlacklistTokensController {

    @Autowired
    private BlacklistTokensService blacklistTokensService;

    @PostMapping("/blacklist")
    public ResponseEntity<String> addTokenInBlacklist(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring("Bearer ".length());
        blacklistTokensService.addTokenToBlacklist(token);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/blacklist")
    public ResponseEntity<Boolean> isTokenInBlacklist(@RequestParam("token") String token) {
        return new ResponseEntity<>(blacklistTokensService.isTokenInBlacklist(token), HttpStatus.OK);
    }

}
