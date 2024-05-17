package org.example.configuration;

import jakarta.annotation.PostConstruct;
import org.example.entity.Authority;
import org.example.repository.AuthorityRepository;
import org.example.roles.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthorityConfig {

    @Autowired
    private AuthorityRepository authorityRepository;

    @PostConstruct
    private void addAuthorities() {
        authorityRepository.findByRole(Roles.ROLE_ADMIN).ifPresentOrElse((value) -> value.getId(), () -> {
            authorityRepository.save(new Authority(Roles.ROLE_ADMIN));});
        authorityRepository.findByRole(Roles.ROLE_USER).ifPresentOrElse((value) -> value.getId(), () -> {
            authorityRepository.save(new Authority(Roles.ROLE_USER));});
        authorityRepository.findByRole(Roles.ROLE_EXECUTOR).ifPresentOrElse((value) -> value.getId(), () -> {
            authorityRepository.save(new Authority(Roles.ROLE_EXECUTOR));});
    }
}