package org.example.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TaskController {

    @GetMapping("/task")
    @RolesAllowed({"USER", "ADMIN", "EXECUTOR"})
    public ResponseEntity createTask() {
        return null;
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader("Authorization").substring("Bearer ".length());
    }
}
