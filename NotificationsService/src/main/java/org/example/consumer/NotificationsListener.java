package org.example.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.MessageDTO;
import org.example.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationsListener {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SendMailService sendMailService;

    @KafkaListener(topics = "notifications", groupId = "notification-group")
    @SneakyThrows
    private void notificationsTopicListener(String data) {
        MessageDTO message = objectMapper.readValue(data, MessageDTO.class);
        sendMailService.sendEmail(message.getEmail(), message.getTitle(), message.getMessage());
    }

}