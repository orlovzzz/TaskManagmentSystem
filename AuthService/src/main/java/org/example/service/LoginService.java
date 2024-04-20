package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.dto.AccountDTO;
import org.example.dto.ResponseDTO;
import org.example.dto.ResponseFromAccountServiceDTO;
import org.example.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoginService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${account.service.url}")
    private String URL;
    @Autowired
    private JwtUtils jwtUtils;

    @SneakyThrows
    public ResponseDTO login(AccountDTO account) {
        HttpEntity request = new HttpEntity<>(account);
        ResponseEntity<String> response = restTemplate.exchange(URL + "/checkAccount", HttpMethod.POST, request, String.class);
        ResponseFromAccountServiceDTO responseDTO = objectMapper.readValue(response.getBody(), ResponseFromAccountServiceDTO.class);
        if (!responseDTO.isSuccess()) {
            return new ResponseDTO(false, "Wrong email or password");
        }
        String jwt = jwtUtils.generateToken(responseDTO.getAccount().getId(), account.getEmail(), account.getPassword());
        return new ResponseDTO(true, jwt);
    }
}