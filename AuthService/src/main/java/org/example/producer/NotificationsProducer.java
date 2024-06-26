package org.example.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationsProducer {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private final String TOPIC_NAME = "notifications";

    @SneakyThrows
    public void addMessageToNotificationsTopic(MessageDTO messageDTO) {
        String json = objectMapper.writeValueAsString(messageDTO);
        kafkaTemplate.send(TOPIC_NAME, json);
    }

}