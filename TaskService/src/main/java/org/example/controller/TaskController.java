package org.example.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.TaskDTO;
import org.example.dto.TaskWithExecutorsDTO;
import org.example.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/task")
    @RolesAllowed({"USER", "ADMIN", "EXECUTOR"})
    public ResponseEntity createTask(HttpServletRequest request, @RequestBody TaskDTO task) {
        taskService.createTask(getTokenFromHeader(request), task);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/task")
    @RolesAllowed({"USER", "ADMIN", "EXECUTOR"})
    public ResponseEntity<List<TaskDTO>> getTasks() {
        return new ResponseEntity<>(taskService.getTasks(), HttpStatus.OK);
    }

    @PostMapping("/task/executor/{taskId}")
    @RolesAllowed({"ADMIN", "EXECUTOR"})
    public ResponseEntity setExecutorToTask(@PathVariable String taskId, HttpServletRequest request) {
        taskService.setExecutorToTask(taskId, getTokenFromHeader(request));
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/task/{taskId}")
    @RolesAllowed({"USER", "ADMIN", "EXECUTOR"})
    public ResponseEntity<TaskWithExecutorsDTO> getTaskById(@PathVariable String taskId, HttpServletRequest request) {
        return new ResponseEntity<>(taskService.getTaskById(taskId, getTokenFromHeader(request)), HttpStatus.OK);
    }

    @GetMapping("/task/account/{accountId}")
    @RolesAllowed({"USER", "ADMIN", "EXECUTOR"})
    public ResponseEntity<List<TaskDTO>> getTasksByAccountId(@PathVariable String accountId) {
        return new ResponseEntity<>(taskService.getTasksByAccountId(accountId), HttpStatus.OK);
    }

    @PostMapping("/task/status/{taskId}")
    @RolesAllowed({ "EXECUTOR" })
    public ResponseEntity setTaskStatus(@PathVariable String taskId,
                                        @RequestParam("status") String status,
                                        HttpServletRequest request) {
        System.out.println(status);
        taskService.setTaskStatus(taskId, status, getTokenFromHeader(request));
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/task/executor/{accountId}")
    @RolesAllowed({"USER", "ADMIN", "EXECUTOR"})
    public ResponseEntity<List<TaskDTO>> getTasksWhereAccountExecutor(@PathVariable String accountId) {
        return new ResponseEntity<>(taskService.getTasksWhereAccountExecutor(accountId), HttpStatus.OK);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}
