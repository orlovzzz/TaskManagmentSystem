package org.example.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.example.entity.Account;
import org.example.entity.Tasks;
import org.example.service.TaskService;
import org.example.views.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Tag(name = "Task Controller", description = "Add, delete, view, change tasks")
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @JsonView(Views.ViewForTasks.class)
    @GetMapping("")
    @Operation(summary = "Get all user task")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Tasks not found"), @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Tasks.class)))})
    public ResponseEntity getAllTasks(HttpServletRequest request) {
        return taskService.getAllTasks(Arrays.stream(request.getCookies()).toList());
    }

    @JsonView(Views.ViewForTasks.class)
    @GetMapping("/{id}")
    @Operation(summary = "Get user task by task ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Task not found"), @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Tasks.class)))})
    public ResponseEntity getTaskById(@PathVariable int id) {
        return taskService.getTaskById(id);
    }

    @PostMapping("")
    @Operation(summary = "Add task")
    public ResponseEntity addTask(HttpServletRequest request, @RequestBody Tasks task) {
        String jwt = taskService.getJwtFromCookies(Arrays.stream(request.getCookies()).toList());
        return taskService.addTask(task, jwt);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Task not found"), @ApiResponse(responseCode = "403", description = "You can`t delete this task"), @ApiResponse(responseCode = "200", description = "Task deleted")})
    public ResponseEntity deleteTask(HttpServletRequest request, @PathVariable int id) {
        String jwt = taskService.getJwtFromCookies(Arrays.stream(request.getCookies()).toList());
        return taskService.deleteTask(jwt, id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Change task by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Task not found"), @ApiResponse(responseCode = "403", description = "You can`t change this task"), @ApiResponse(responseCode = "400", description = "This priority not allowed.\n" + "Status could be HIGH, MEDIUM, LOW"), @ApiResponse(responseCode = "404", description = "Executor not found"), @ApiResponse(responseCode = "200", description = "Task changed")})
    public ResponseEntity changeTask(@RequestBody Tasks task, HttpServletRequest request, @PathVariable int id) {
        String jwt = taskService.getJwtFromCookies(Arrays.stream(request.getCookies()).toList());
        return taskService.changeTask(task, jwt, id);
    }

    @JsonView(Views.ViewForTasks.class)
    @GetMapping("/executor")
    @Operation(summary = "Get all tasks where user is executor")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "You are not any task executor"), @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Tasks.class)))})
    public ResponseEntity getTasksWhereCurrentAccountExecutor(HttpServletRequest request) {
        String jwt = taskService.getJwtFromCookies(Arrays.stream(request.getCookies()).toList());
        return taskService.getTasksWhereCurrentAccountExecutor(jwt);
    }

    @PatchMapping("/executor/{id}")
    @Operation(summary = "Change task status by ID")
    public ResponseEntity changeStatus(@RequestBody Tasks task, HttpServletRequest request, @PathVariable int id) {
        String jwt = taskService.getJwtFromCookies(Arrays.stream(request.getCookies()).toList());
        return taskService.changeStatus(task, jwt, id);
    }

}
