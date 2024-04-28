package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.dto.AccountDTO;
import org.example.dto.ResponseDTO;
import org.example.dto.ResponseFromAccountServiceDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RegistrationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${account.service.url}")
    private String URL;

    @SneakyThrows
    public ResponseDTO registration(AccountDTO account) {
        HttpEntity<AccountDTO> request = new HttpEntity<>(account);
        ResponseEntity<String> response = restTemplate.exchange(URL + "/createAccount", HttpMethod.POST, request, String.class);
        ResponseFromAccountServiceDTO responseFromService = objectMapper.readValue(response.getBody(), ResponseFromAccountServiceDTO.class);
        if (responseFromService.isSuccess()) {
            return new ResponseDTO(true, "");
        }
        return new ResponseDTO(false, "An account with this email has already been registered");
    }

}