package org.example.service;

import jakarta.servlet.http.Cookie;
import org.example.entity.Account;
import org.example.entity.Tasks;
import org.example.entity.enumeration.Priority;
import org.example.entity.enumeration.Status;
import org.example.repository.AccountRepository;
import org.example.repository.CommentsRepository;
import org.example.repository.TaskRepository;
import org.example.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CommentsRepository commentsRepository;

    public ResponseEntity getAllTasks(List<Cookie> cookies) {
        String jwt = getJwtFromCookies(cookies);
        String email = jwtUtil.getEmailFromToken(jwt);
        Account account = accountRepository.findByEmail(email);
        List <Tasks> tasks = taskRepository.findByAuthor(account);
        if (tasks.isEmpty()) {
            return new ResponseEntity("Tasks not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(tasks, HttpStatus.OK);
    }

    public ResponseEntity getTaskById(int id) {
        Optional<Tasks> tasks = taskRepository.findById(id);
        if (tasks.isEmpty()) {
            return new ResponseEntity("Task with id: " + id + " not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(tasks, HttpStatus.OK);
    }

    public ResponseEntity addTask(Tasks task, String token) {
        Account account = accountRepository.findByEmail(jwtUtil.getEmailFromToken(token));
        if (account == null) {
            return new ResponseEntity("Account not found", HttpStatus.NOT_FOUND);
        }
        task.setAuthor(account);
        List<Account> executors = task.getExecutors();
        for (int i = 0; i < executors.size(); i++) {
            Account executor = accountRepository.findById(executors.get(i).getId()).orElse(null);
            if (executor == null) {
                return new ResponseEntity("Executor with id: " + executors.get(i).getId() + " not found", HttpStatus.NOT_FOUND);
            }
            executors.set(i, executor);
        }
        task.setExecutors(executors);
        task.setStatus("AWAITING");
        if (task.getPriority() != null && !Priority.isPresent(task.getPriority())) {
            return new ResponseEntity("This priority not allowed.\n" +
                    "Status could be HIGH, MEDIUM, LOW", HttpStatus.BAD_REQUEST);
        }
        taskRepository.save(task);
        return new ResponseEntity("Task created", HttpStatus.CREATED);
    }

    public ResponseEntity deleteTask(String token, int id) {
        Account account = accountRepository.findByEmail(jwtUtil.getEmailFromToken(token));
        Tasks task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return new ResponseEntity("Task with id: " + id + " not found", HttpStatus.NOT_FOUND);
        }
        if (!task.getAuthor().equals(account)) {
            return new ResponseEntity("You can`t delete this task", HttpStatus.FORBIDDEN);
        }
        taskRepository.delete(task);
        return new ResponseEntity("Task deleted", HttpStatus.OK);
    }

    public ResponseEntity changeTask(Tasks taskDTO, String token, int id) {
        Tasks task = taskRepository.findById(id).orElse(null);
        String email = jwtUtil.getEmailFromToken(token);
        Account account = accountRepository.findByEmail(email);
        if (task == null) {
            return new ResponseEntity("Task with id: " + id + " not found", HttpStatus.NOT_FOUND);
        }
        if (!task.getAuthor().equals(account)) {
            return new ResponseEntity("You can`t change this task", HttpStatus.FORBIDDEN);
        }
        if (taskDTO.getHeader() != null) {
            task.setHeader(taskDTO.getHeader());
        }
        if (taskDTO.getDescript() != null) {
            task.setDescript(taskDTO.getDescript());
        }
        if (taskDTO.getPriority() != null) {
            if (!Priority.isPresent(taskDTO.getPriority())) {
                return new ResponseEntity("This priority not allowed.\n" +
                        "Status could be HIGH, MEDIUM, LOW", HttpStatus.BAD_REQUEST);
            }
            task.setPriority(taskDTO.getPriority());
        }
        if (taskDTO.getExecutors() != null) {
            for (int i = 0; i < taskDTO.getExecutors().size(); i++) {
                Account executor = accountRepository.findById(taskDTO.getExecutors().get(i).getId()).orElse(null);
                if (executor == null) {
                    return new ResponseEntity("Executor with id: " + taskDTO.getExecutors().get(i).getId() + " not found", HttpStatus.NOT_FOUND);
                }
                taskDTO.getExecutors().set(i, executor);
            }
            task.setExecutors(taskDTO.getExecutors());
        }
        if (taskDTO.getComments() != null) {
            for (int i = 0; i < taskDTO.getComments().size(); i++) {
                commentsRepository.save(taskDTO.getComments().get(i));
                taskDTO.getComments().set(i, commentsRepository.findById(taskDTO.getComments().get(i).getId()).orElse(null));
            }
            task.setComments(taskDTO.getComments());
        }
        taskRepository.save(task);
        return new ResponseEntity("Task changed", HttpStatus.OK);
    }

    public ResponseEntity changeStatus(Tasks taskDTO, String token, int id) {
        Account currentAccount = accountRepository.findByEmail(jwtUtil.getEmailFromToken(token));
        Tasks task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return new ResponseEntity("Task with id: " + id + " not found", HttpStatus.NOT_FOUND);
        }
        if (!task.getExecutors().contains(currentAccount)) {
            return new ResponseEntity("You can`t change status this task", HttpStatus.FORBIDDEN);
        }
        if (taskDTO.getStatus() != null) {
            if (!Status.isPresent(taskDTO.getStatus())) {
                return new ResponseEntity("This status not allowed.\n" +
                        "Status could be AWAITING, IN_PROCESSING, COMPLETED", HttpStatus.BAD_REQUEST);
            }
        }
        task.setStatus(taskDTO.getStatus());
        taskRepository.save(task);
        return new ResponseEntity("Task status changed", HttpStatus.OK);
    }

    public ResponseEntity getTasksWhereCurrentAccountExecutor(String token) {
        Account currentAccount = accountRepository.findByEmail(jwtUtil.getEmailFromToken(token));
        List<Tasks> tasksWhereAccountExecutor = taskRepository.findByExecutors(currentAccount);
        if (tasksWhereAccountExecutor == null) {
            return new ResponseEntity("You are not any task executor", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(tasksWhereAccountExecutor, HttpStatus.OK);
    }

    public String getJwtFromCookies(List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("jwt")) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
