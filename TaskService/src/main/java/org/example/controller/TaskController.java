package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Task controller", description = "Create, change, get, delete task. Add comments")
@RestController
@RequestMapping("/taskApi")
@CrossOrigin
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Create task")
    @PostMapping("/task")
    @RolesAllowed({"USER", "ADMIN", "EXECUTOR"})
    public ResponseEntity createTask(HttpServletRequest request, @RequestBody TaskDTO task) {
        taskService.createTask(getTokenFromHeader(request), task);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Operation(summary = "Get all tasks. " +
            "You can filter response by id, title, description, priority, status and author id. Filter params not required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "No such field", content = @Content)
    })
    @GetMapping("/task")
    @RolesAllowed({"USER", "ADMIN", "EXECUTOR"})
    public ResponseEntity<List<TaskDTO>> getTasks(@RequestParam(name = "field", required = false) String field,
                                                  @RequestParam(name = "pattern", required = false) String pattern) {
        if (field != null) {
            try {
                return new ResponseEntity<>(taskService.getAllTasksWithFilter(field, pattern), HttpStatus.OK);
            } catch (NoSuchFieldException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(taskService.getTasks(), HttpStatus.OK);
    }

    @Operation(summary = "Set executor to task. You will become executor. Method take executor id from your JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PostMapping("/task/executor/{taskId}")
    @RolesAllowed({"ADMIN", "EXECUTOR"})
    public ResponseEntity setExecutorToTask(@PathVariable String taskId, HttpServletRequest request) {
        try {
            taskService.setExecutorToTask(taskId, getTokenFromHeader(request));
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content())
    })
    @GetMapping("/task/{taskId}")
    @RolesAllowed({"USER", "ADMIN", "EXECUTOR"})
    public ResponseEntity<TaskWithExecutorsDTO> getTaskById(@PathVariable String taskId, HttpServletRequest request) {
        try {
            TaskWithExecutorsDTO task = taskService.getTaskById(taskId, getTokenFromHeader(request));
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Set status to task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "400", description = "Not the executor of this task")
    })
    @PostMapping("/task/status/{taskId}")
    @RolesAllowed({ "EXECUTOR" })
    public ResponseEntity setTaskStatus(@PathVariable String taskId,
                                        @RequestParam("status") String status,
                                        HttpServletRequest request) {
        try {
            taskService.setTaskStatus(taskId, status, getTokenFromHeader(request));
            return new ResponseEntity(HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get all task by account id")
    @GetMapping("/task/account/{accountId}")
    @RolesAllowed({"USER", "ADMIN", "EXECUTOR"})
    public ResponseEntity<List<TaskWithExecutorsDTO>> getTasksByAccountId(HttpServletRequest request, @PathVariable String accountId) {
        return new ResponseEntity<>(taskService.getTasksByAccountId(getTokenFromHeader(request), accountId), HttpStatus.OK);
    }

    @Operation(summary = "Get all task where account executor")
    @GetMapping("/task/executor/{accountId}")
    @RolesAllowed({"USER", "ADMIN", "EXECUTOR"})
    public ResponseEntity<List<TaskDTO>> getTasksWhereAccountExecutor(@PathVariable String accountId) {
        return new ResponseEntity<>(taskService.getTasksWhereAccountExecutor(accountId), HttpStatus.OK);
    }

    @Operation(summary = "Delete task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "400", description = "Don`t author of this task")
    })
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

    @Operation(summary = "Change task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "400", description = "Don`t author of this task")
    })
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

    @Operation(summary = "Add comment to task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
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
