package org.example.service;

import org.example.dto.AccountDTO;
import org.example.dto.AccountWithRolesDTO;
import org.example.dto.MessageDTO;
import org.example.entity.Account;
import org.example.mapper.AccountMapper;
import org.example.repository.AccountRepository;
import org.example.repository.AuthorityRepository;
import org.example.roles.Roles;
import org.example.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private AccountMapper accountMapper;
    private AccountRepository accountRepository;
    private JwtUtils jwtUtils;
    private AuthorityRepository authorityRepository;
    private NotificationProducer producer;

    @Autowired
    public AccountService(AccountMapper accountMapper, AccountRepository accountRepository, JwtUtils jwtUtils,
                          AuthorityRepository authorityRepository, NotificationProducer producer) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
        this.jwtUtils = jwtUtils;
        this.authorityRepository = authorityRepository;
        this.producer = producer;
    }

    public List<AccountWithRolesDTO> getAllAccounts(String token) {
        UUID id = UUID.fromString(jwtUtils.extractAccountId(token));
        List<AccountWithRolesDTO> accountsList = accountRepository.findAll()
                .stream().filter(t -> !t.getId().equals(id))
                .map(o -> accountMapper.fromEntityToRolesDTO(o)).collect(Collectors.toList());
        return accountsList;
    }

    public AccountWithRolesDTO getAccountById(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account not found, id = " + id));
        return accountMapper.fromEntityToRolesDTO(account);
    }

    public void setAccountToExecutorRole(String token) {
        UUID id = UUID.fromString(jwtUtils.extractAccountId(token));
        Account account = accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setAuthority(authorityRepository.findByRole(Roles.ROLE_EXECUTOR).orElseThrow(() -> new IllegalArgumentException("Role not found")));
        producer.addMessageToNotificationsTopic(new MessageDTO(account.getEmail(), "Your role", "You have successfully become a executor."));
        accountRepository.save(account);
    }

    public void deleteAccount(UUID id) {
        accountRepository.delete(accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account not found")));
    }

    public void changeAccount(AccountDTO accountDTO) {
        Account account = accountRepository.findById(accountDTO.getId()).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if (!accountDTO.getEmail().isEmpty()) {
            account.setEmail(accountDTO.getEmail());
        }
        if (!accountDTO.getPassword().isEmpty()) {
            account.setPassword(accountDTO.getPassword());
        }
        accountRepository.save(account);
    }

    public AccountWithRolesDTO getMyAccount(String token) {
        return getAccountById(UUID.fromString(jwtUtils.extractAccountId(token)));
    }

}
