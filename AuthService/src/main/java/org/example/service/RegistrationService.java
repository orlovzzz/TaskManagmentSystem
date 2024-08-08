package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.dto.AccountDTO;
import org.example.dto.MessageDTO;
import org.example.dto.ResponseDTO;
import org.example.dto.ResponseFromAccountServiceDTO;
import org.example.producer.NotificationsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RegistrationService {

    @Autowired
    private NotificationsProducer notificationsProducer;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${account.service.url}")
    private String URL;

    @SneakyThrows
    public boolean registration(AccountDTO account) {
        HttpEntity<AccountDTO> request = new HttpEntity<>(account);
        ResponseEntity<String> response = restTemplate.exchange(URL + "/createAccount", HttpMethod.POST, request, String.class);
        ResponseFromAccountServiceDTO responseFromService = objectMapper.readValue(response.getBody(), ResponseFromAccountServiceDTO.class);
        if (!responseFromService.isSuccess()) {
            return false;
        }
        notificationsProducer.addMessageToNotificationsTopic(new MessageDTO(account.getEmail(), "Registration",
                "You have successfully registered.\nDon't forget your password: " + account.getPassword()));
        return true;
    }
}