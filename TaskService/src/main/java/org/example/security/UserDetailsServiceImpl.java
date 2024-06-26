package org.example.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.example.dto.AccountDTO;
import org.example.dto.AccountWithRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Value("${account.service.url}")
    private String ACCOUNT_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Gson gson = new Gson();
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            AccountWithRoleDTO accountWithRoleDTO = getAccount(username);
            return new UserDetailsImpl(accountWithRoleDTO.getEmail(), accountWithRoleDTO.getPassword(), accountWithRoleDTO.getAuthority());
        } catch(Exception e) {
            throw new UsernameNotFoundException("Account not found");
        }
    }

    public AccountWithRoleDTO getAccount(String token) throws JsonProcessingException {
        String id = jwtUtils.extractAccountId(token.substring("Bearer ".length()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(ACCOUNT_URL + "/accounts/" + id,
                HttpMethod.GET, entity, String.class);
        return gson.fromJson(response.getBody(), AccountWithRoleDTO.class);
    }
}
