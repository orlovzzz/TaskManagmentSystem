package org.example.service;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.example.dto.AccountDTO;
import org.example.dto.MessageDTO;
import org.example.dto.TaskDTO;
import org.example.dto.TaskWithExecutorsDTO;
import org.example.entity.ExecutorsTasks;
import org.example.entity.ExecutorsTasksId;
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
import java.util.UUID;
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
    private final String BEARER_PREFIX = "Bearer ";
    @Autowired
    private NotificationProducer producer;

    public void createTask(String token, TaskDTO taskDTO) {
        AccountDTO account = getAccount(token, jwtUtils.extractAccountId(token.substring(BEARER_PREFIX.length())));
        Task task = taskMapper.fromDTO(taskDTO);
        task.setAuthorId(account.getId());
        task.setStatus(Status.PENDING);
        producer.addMessageToNotificationsTopic(new MessageDTO(account.getEmail(), "Create task",
                        "You have successfully created a task.\n" +
                                "ID: " + task.getId() + "\nTitle: " + task.getTitle() + "\nDescription: " + task.getDescription() +
                "\nPriority: " + task.getPriority()));
        taskRepository.save(task);
    }

    public List<TaskDTO> getTasks() {
        List<TaskDTO> taskDTO = taskRepository.findAll().stream()
                .map(o -> taskMapper.fromEntity(o))
                .peek(o -> o.setExecutorsCount(executorsTaskRepository.countExecutors(o.getId())))
                .collect(Collectors.toList());
        return taskDTO;
    }

    public void setExecutorToTask(String taskId, String token) {
        UUID executorId = UUID.fromString(jwtUtils.extractAccountId(token.substring(BEARER_PREFIX.length())));
        Task task = taskRepository.findById(UUID.fromString(taskId)).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Thread thread = new Thread(() -> {
            AccountDTO account = getAccount(token, executorId);
            AccountDTO author = getAccount(token, task.getAuthorId());
            producer.addMessageToNotificationsTopic(new MessageDTO(author.getEmail(), "Executor take your task",
                    "Your task will be handled by the executor with \nID: " + account.getId() + "\nEmail: " + account.getEmail()));
            System.out.println("SENDSENDSENDSENDSENDSENDSENDSENDSENDSENDSENDSENDSENDSEND");
        });
        thread.start();
        executorsTaskRepository.save(new ExecutorsTasks(new ExecutorsTasksId(executorId, task)));
    }

    public TaskWithExecutorsDTO getTaskById(String taskId, String token) {
        List<ExecutorsTasks> executorsTasks = executorsTaskRepository.findByTaskId(UUID.fromString(taskId));
        TaskWithExecutorsDTO taskWithExecutors = taskMapper.fromEntityForExecutorsTask(
                taskRepository.findById(UUID.fromString(taskId))
                        .orElseThrow(() -> new IllegalArgumentException("Task not found")));
        taskWithExecutors.setExecutors(executorsTasks.stream()
                .map(o -> getAccount(token, o.getExecutorsTasksId().getExecutorId()))
                .collect(Collectors.toSet()));
        return taskWithExecutors;
    }

    public List<TaskDTO> getTasksByAccountId(String accountId) {
        List<TaskDTO> tasks = taskRepository.findByAuthorId(UUID.fromString(accountId)).stream()
                .map(o -> taskMapper.fromEntity(o))
                .peek(o -> o.setExecutorsCount(executorsTaskRepository.countExecutors(o.getId())))
                .collect(Collectors.toList());
        return tasks;
    }

    public void setTaskStatus(String taskId, String status, String token) {
        Task task = taskRepository.findById(UUID.fromString(taskId))
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Status beforeStatus = task.getStatus();
        UUID executorId = UUID.fromString(jwtUtils.extractAccountId(token.substring(BEARER_PREFIX.length())));
        executorsTaskRepository.findById(new ExecutorsTasksId(executorId, task))
                .orElseThrow(() -> new IllegalArgumentException("Not the executor of this task"));
        Thread thread = new Thread(() -> {
            AccountDTO author = getAccount(token, task.getAuthorId());
            producer.addMessageToNotificationsTopic(new MessageDTO(author.getEmail(), "Your task status has changed",
                    "The task executor with ID " + executorId + " changed the task status from " + beforeStatus + " to " + Status.valueOf(status) + "."));
        });
        thread.start();
        task.setStatus(Status.valueOf(status));
        taskRepository.save(task);
    }

    public List<TaskDTO> getTasksWhereAccountExecutor(String accountId) {
        List<ExecutorsTasks> executorsTasks = executorsTaskRepository.findByExecutorId(UUID.fromString(accountId));
        List<TaskDTO> task = executorsTasks.stream().map(o -> taskMapper.fromEntity(o.getExecutorsTasksId().getTask())).collect(Collectors.toList());
        return task;
    }

    @SneakyThrows
    private AccountDTO getAccount(String token, String stringId) {
        UUID id = UUID.fromString(stringId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(ACCOUNT_URL + "/accounts/" + id,
                HttpMethod.GET, entity, String.class);
        return gson.fromJson(response.getBody(), AccountDTO.class);
    }

    @SneakyThrows
    private AccountDTO getAccount(String token, UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(ACCOUNT_URL + "/accounts/" + id,
                HttpMethod.GET, entity, String.class);
        return gson.fromJson(response.getBody(), AccountDTO.class);
    }

}
