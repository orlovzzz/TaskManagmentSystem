package org.example.service;

import org.example.entity.Account;
import org.example.repository.AccountRepository;
import org.example.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity createAccount(Account account) {
        String email = account.getEmail();
        if (!(email.contains("@") && email.contains("."))) {
            return new ResponseEntity("Wrong email", HttpStatus.BAD_REQUEST);
        }
        if (accountRepository.findByEmail(account.getEmail()) != null) {
            return new ResponseEntity("User with email " + email + " already exist", HttpStatus.BAD_REQUEST);
        }
        if (account.getPassword().length() < 8) {
            return new ResponseEntity("The password must be at least 8 characters long", HttpStatus.BAD_REQUEST);
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
        return new ResponseEntity("Account created", HttpStatus.CREATED);
    }

    public ResponseEntity login(Account account) {
        Account acc = accountRepository.findByEmail(account.getEmail());
        if (acc == null) {
            return new ResponseEntity("Account not found", HttpStatus.NOT_FOUND);
        }
        if (!passwordEncoder.matches(account.getPassword(), acc.getPassword())) {
            return new ResponseEntity("Password incorrect", HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword()));
        String jwt = jwtUtil.generateAccessToken(account.getEmail());
        return new ResponseEntity(jwt, HttpStatus.ACCEPTED);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

}
