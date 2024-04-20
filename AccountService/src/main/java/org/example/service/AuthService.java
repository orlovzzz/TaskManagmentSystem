package org.example.service;

import org.example.dto.AccountDTO;
import org.example.dto.ResponseDTO;
import org.example.entity.Account;
import org.example.mapper.AccountMapper;
import org.example.repository.AccountRepository;
import org.example.repository.AuthorityRepository;
import org.example.roles.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    public ResponseDTO checkUserForLogin(AccountDTO accountDTO) {
        Account account = accountRepository.findByEmail(accountDTO.getEmail()).orElse(null);
        if (account == null) {
            return new ResponseDTO(false, null);
        }
        if (!passwordEncoder.matches(accountDTO.getPassword(), account.getPassword())) {
            return new ResponseDTO(false, null);
        }
        return new ResponseDTO(true, accountMapper.fromEntity(account));
    }

    public ResponseDTO checkUserForReg(AccountDTO accountDTO) {
        Account account = accountRepository.findByEmail(accountDTO.getEmail()).orElse(null);
        if (account != null) {
            return new ResponseDTO(false, null);
        }
        account = accountMapper.fromDto(accountDTO);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAuthority(authorityRepository.findByRole(Roles.USER).orElse(null));
        accountRepository.save(account);
        return new ResponseDTO(true, null);
    }
}