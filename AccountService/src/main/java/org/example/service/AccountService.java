package org.example.service;

import org.example.repository.AccountRepository;
import org.example.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private JwtUtils jwtUtils;

    public void getAllAccounts(String token) {
        System.out.println(jwtUtils.extractSubject(token));
    }

}
