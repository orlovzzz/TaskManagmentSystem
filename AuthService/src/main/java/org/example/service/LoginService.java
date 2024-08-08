package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.dto.AccountDTO;
import org.example.dto.MessageDTO;
import org.example.dto.ResponseDTO;
import org.example.dto.ResponseFromAccountServiceDTO;
import org.example.jwt.JwtUtils;
import org.example.producer.NotificationsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class LoginService {
    @Autowired
    private NotificationsProducer notificationsProducer;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private NotificationsProducer producer;
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

        producer.addMessageToNotificationsTopic(new MessageDTO(account.getEmail(),
                "Login", "The account was logged in at "
                + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yy"))));
        return new ResponseDTO(true, "Bearer " + jwt);
    }
}