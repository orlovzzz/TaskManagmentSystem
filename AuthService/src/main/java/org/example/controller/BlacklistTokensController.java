package org.example.controller;

import org.example.service.BlacklistTokensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlacklistTokensController {

    @Autowired
    private BlacklistTokensService blacklistTokensService;

    @PostMapping("/blacklist")
    public ResponseEntity<String> addTokenInBlacklist(@RequestParam("token") String token) {;
        blacklistTokensService.addTokenToBlacklist(token);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/blacklist")
    public ResponseEntity<Boolean> isTokenInBlacklist(@RequestParam("token") String token) {
        return new ResponseEntity<>(blacklistTokensService.isTokenInBlacklist(token), HttpStatus.OK);
    }

}
