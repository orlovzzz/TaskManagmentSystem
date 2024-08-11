package org.example.service;

import org.checkerframework.checker.units.qual.A;
import org.example.dto.AccountDTO;
import org.example.dto.AccountWithRolesDTO;
import org.example.entity.Account;
import org.example.entity.Authority;
import org.example.mapper.AccountMapper;
import org.example.mapper.AccountMapperImpl;
import org.example.mapper.AuthorityMapperImpl;
import org.example.repository.AccountRepository;
import org.example.repository.AuthorityRepository;
import org.example.roles.Roles;
import org.example.security.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthorityRepository authorityRepository;

    private AccountMapper accountMapper = new AccountMapperImpl(new AuthorityMapperImpl());

    private static final byte[] bytes = {1};
    private static final List<Account> accounts = new ArrayList<>();

    @BeforeAll
    public static void init() {
        Account account1 = new Account("test1", "test1", new Authority(Roles.ROLE_USER));
        account1.setId(UUID.nameUUIDFromBytes(bytes));
        Account account2 = new Account("test2", "test2", new Authority(Roles.ROLE_EXECUTOR));
        Account account3 = new Account("test3", "test3", new Authority(Roles.ROLE_ADMIN));
        accounts.addAll(List.of(account1, account2, account3));
    }

    @Test
    void getAllAccounts() {
        accountService.setAccountMapper(accountMapper);
        Mockito.when(jwtUtils.extractAccountId(anyString())).thenReturn(UUID.nameUUIDFromBytes(bytes).toString());
        Mockito.when(accountRepository.findAll()).thenReturn(accounts);

        List<AccountWithRolesDTO> accountsTest = accountService.getAllAccounts("token");
        List<AccountWithRolesDTO> expectedAccounts = accounts.stream().map(o -> accountMapper.fromEntityToRolesDTO(o)).collect(Collectors.toList());

        Assertions.assertEquals(expectedAccounts, accountsTest);
    }

    @Test
    void getAccountById() {
        accountService.setAccountMapper(accountMapper);
        UUID accountId = UUID.nameUUIDFromBytes(bytes);
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.ofNullable(accounts.get(0)));

        AccountWithRolesDTO accountTest = accountService.getAccountById(accountId);
        AccountWithRolesDTO accountExpected = accountMapper.fromEntityToRolesDTO(accounts.get(0));

        Assertions.assertEquals(accountExpected, accountTest);
    }

    @Test
    void getAccountById_ThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> accountService.getAccountById(UUID.fromString("1")));
    }
}