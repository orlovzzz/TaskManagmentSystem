package org.example.service;

import com.google.gson.Gson;
import lombok.Setter;
import lombok.SneakyThrows;
import org.example.dto.*;
import org.example.entity.Comments;
import org.example.entity.ExecutorsTasks;
import org.example.entity.ExecutorsTasksId;
import org.example.entity.Task;
import org.example.enums.Priority;
import org.example.enums.Status;
import org.example.mapper.TaskListMapper;
import org.example.mapper.TaskMapper;
import org.example.repository.*;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Setter
public class TaskService {
    @Value("${account.service.url}")
    private String ACCOUNT_URL;
    private JwtUtils jwtUtils;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Gson gson = new Gson();
    private TaskRepository taskRepository;
    private TaskMapper taskMapper;
    private ExecutorsTaskRepository executorsTaskRepository;
    private final String BEARER_PREFIX = "Bearer ";
    private NotificationProducer producer;
    private CommentsRepository commentsRepository;

    @Autowired
    public TaskService(JwtUtils jwtUtils, TaskRepository taskRepository,
            TaskMapper taskMapper, ExecutorsTaskRepository executorsTaskRepository, NotificationProducer producer,
            CommentsRepository commentsRepository) {
        this.jwtUtils = jwtUtils;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.executorsTaskRepository = executorsTaskRepository;
        this.producer = producer;
        this.commentsRepository = commentsRepository;
    }

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

    public List<TaskWithExecutorsDTO> getTasksByAccountId(String token, String accountId) {
        List<Task> task = taskRepository.findByAuthorId(UUID.fromString(accountId));
        List<TaskWithExecutorsDTO> tasks = new ArrayList<>(task.size());
        for (Task t : task) {
            TaskWithExecutorsDTO twe = taskMapper.fromEntityForExecutorsTask(t);
            twe.setExecutors(t.getExecutorsTasks().stream()
                    .map(o -> getAccount(token, o.getExecutorsTasksId().getExecutorId()))
                    .collect(Collectors.toSet()));
            tasks.add(twe);
        }
        return tasks;
    }

    public void setTaskStatus(String taskId, String status, String token) {
        Task task = taskRepository.findById(UUID.fromString(taskId))
                .orElseThrow(() -> new NullPointerException("Task not found"));
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
        List<TaskDTO> task = executorsTasks.stream()
                .map(o -> taskMapper.fromEntity(o.getExecutorsTasksId().getTask()))
                .peek(o -> o.setExecutorsCount(executorsTaskRepository.countExecutors(o.getId())))
                .collect(Collectors.toList());
        return task;
    }

    public void deleteTask(String taskId, String token) {
        Task task = taskRepository.findById(UUID.fromString(taskId)).orElseThrow(() -> new NullPointerException());
        if (!task.getAuthorId().equals(UUID.fromString(jwtUtils.extractAccountId(token.substring(BEARER_PREFIX.length()))))) {
            throw new IllegalArgumentException();
        }
        taskRepository.delete(task);
    }

    public void changeTask(String token, ChangeTaskDTO taskDTO) {
        Task task = taskRepository.findById(UUID.fromString(taskDTO.getId())).orElseThrow(() -> new NullPointerException());
        if (!task.getAuthorId().equals(UUID.fromString(jwtUtils.extractAccountId(token.substring(BEARER_PREFIX.length()))))) {
            throw new IllegalArgumentException();
        }
        if (taskDTO.getDescription() != null) {
            task.setDescription(taskDTO.getDescription());
        }
        if (taskDTO.getPriority() != null) {
            task.setPriority(Priority.valueOf(taskDTO.getPriority()));
        }
        if (taskDTO.getTitle() != null) {
            task.setTitle(taskDTO.getTitle());
        }
        taskRepository.save(task);
    }

    public void addComment(CommentDTO commentDTO, String token) {
        Task task = taskRepository.findById(UUID.fromString(commentDTO.getTaskId())).orElseThrow(() -> new NullPointerException());
        String email = jwtUtils.extractEmail(token.substring(BEARER_PREFIX.length()));
        Comments comment = new Comments(email, commentDTO.getComment(), task);
        task.getComments().add(comment);
        taskRepository.save(task);
    }

    public List<TaskDTO> getAllTasksWithFilter(String field, String pattern) throws NoSuchFieldException {
        boolean isFieldPresent = Arrays.stream(Task.class.getDeclaredFields()).peek(o -> o.setAccessible(true))
                .anyMatch(f -> f.getName().equals(field));
        if(!isFieldPresent) throw new NoSuchFieldException();
        CustomerSpecs spec = new CustomerSpecs(new SearchCriteria(field, pattern));
        List<TaskDTO> taskDTO = taskRepository.findAll(spec).stream()
                .map(o -> taskMapper.fromEntity(o))
                .peek(o -> o.setExecutorsCount(executorsTaskRepository.countExecutors(o.getId())))
                .collect(Collectors.toList());
        return taskDTO;
    }

    public AccountDTO getAccount(String token, String stringId) {
        try {
            UUID id = UUID.fromString(stringId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);
            HttpEntity entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(ACCOUNT_URL + "/accounts/" + id,
                    HttpMethod.GET, entity, String.class);
            return gson.fromJson(response.getBody(), AccountDTO.class);
        } catch(Exception e) {
            return null;
        }
    }

    public AccountDTO getAccount(String token, UUID id) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);
            HttpEntity entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(ACCOUNT_URL + "/accounts/" + id,
                    HttpMethod.GET, entity, String.class);
            return gson.fromJson(response.getBody(), AccountDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

}
