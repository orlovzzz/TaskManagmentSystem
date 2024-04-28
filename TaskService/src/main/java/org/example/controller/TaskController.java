package org.example.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.TaskDTO;
import org.example.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
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

    private String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader("Authorization").substring("Bearer ".length());
    }
}
