package org.example.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.ChangeTaskDTO;
import org.example.dto.CommentDTO;
import org.example.dto.TaskDTO;
import org.example.dto.TaskWithExecutorsDTO;
import org.example.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taskApi")
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
        try {
            taskService.setTaskStatus(taskId, status, getTokenFromHeader(request));
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/task/executor/{accountId}")
    @RolesAllowed({"USER", "ADMIN", "EXECUTOR"})
    public ResponseEntity<List<TaskDTO>> getTasksWhereAccountExecutor(@PathVariable String accountId) {
        return new ResponseEntity<>(taskService.getTasksWhereAccountExecutor(accountId), HttpStatus.OK);
    }

    @DeleteMapping("/task/{taskId}")
    @RolesAllowed({"USER", "ADMIN", "EXECUTOR"})
    public ResponseEntity deleteTask(@PathVariable String taskId, HttpServletRequest request) {
        try {
            taskService.deleteTask(taskId, getTokenFromHeader(request));
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (NullPointerException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/task")
    @RolesAllowed({"USER", "ADMIN", "EXECUTOR"})
    public ResponseEntity changeTask(@RequestBody ChangeTaskDTO taskDTO, HttpServletRequest request) {
        try {
            taskService.changeTask(getTokenFromHeader(request), taskDTO);
            return new ResponseEntity(HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/task/comment")
    @RolesAllowed({"USER", "ADMIN", "EXECUTOR"})
    public ResponseEntity addComment(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        try {
            taskService.addComment(commentDTO, getTokenFromHeader(request));
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}
