package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.example.dto.AccountDTO;
import org.example.dto.TaskDTO;
import org.example.entity.Task;
import org.example.enums.Status;
import org.example.mapper.TaskMapper;
import org.example.repository.ExecutorsTaskRepository;
import org.example.repository.TaskRepository;
import org.example.security.JwtUtils;
import org.example.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Value("${account.service.url}")
    private String ACCOUNT_URL;
    @Autowired
    private JwtUtils jwtUtils;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Gson gson = new Gson();
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private ExecutorsTaskRepository executorsTaskRepository;

    @Transactional
    public void createTask(String token, TaskDTO taskDTO) {
        AccountDTO account = getAccount(token);
        Task task = taskMapper.fromDTO(taskDTO);
        task.setAuthorId(account.getId());
        task.setStatus(Status.PENDING);
        taskRepository.save(task);
    }

    public List<TaskDTO> getTasks() {
        List<TaskDTO> taskDTO = taskRepository.findAll().stream()
                .map(o -> taskMapper.fromEntity(o))
                .peek(o -> o.setExecutorsCount(executorsTaskRepository.findByTaskId(o.getId()).size()))
                .collect(Collectors.toList());
        return taskDTO;
    }

    @SneakyThrows
    public AccountDTO getAccount(String token) {
        String id = jwtUtils.extractAccountId(token);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(ACCOUNT_URL + "/accounts/" + id,
                HttpMethod.GET, entity, String.class);
        return gson.fromJson(response.getBody(), AccountDTO.class);
    }

}
