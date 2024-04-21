package org.example.security;

import org.example.entity.Account;
import org.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Account acc = accountRepository.findByEmail(email);

        if (acc == null) throw new UsernameNotFoundException("Login " + email + "not found");

        return new UserDetailsImpl(
                acc.getEmail(),
                acc.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")
        ));
    }
}
