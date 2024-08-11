package org.example.service;

import org.example.dto.AccountDTO;
import org.example.dto.TaskDTO;
import org.example.dto.TaskWithExecutorsDTO;
import org.example.entity.ExecutorsTasks;
import org.example.entity.ExecutorsTasksId;
import org.example.entity.Task;
import org.example.enums.Priority;
import org.example.enums.Status;
import org.example.mapper.*;
import org.example.repository.ExecutorsTaskRepository;
import org.example.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    @Spy
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ExecutorsTaskRepository executorsTaskRepository;

    private final TaskMapperImpl taskMapper = new TaskMapperImpl(new CommentsListMapperImpl(new CommentsMapperImpl()));

    private static List<Task> tasks;
    private static ExecutorsTasks executorsTasks;
    private final static byte[] bytes = {1, 2};

    @BeforeAll
    public static void init() {
        Task task1 = new Task("test1", "test1", Status.PENDING, Priority.LOW);
        task1.setId(UUID.nameUUIDFromBytes(bytes));
        executorsTasks = new ExecutorsTasks(new ExecutorsTasksId(UUID.randomUUID(), task1));
        task1.addToExecutorsTasks(executorsTasks);

        Task task2 = new Task("test2", "test2", Status.PROCESSING, Priority.MEDIUM);
        task2.setAuthorId(UUID.nameUUIDFromBytes(bytes));
        Task task3 = new Task("test3", "test3", Status.COMPLETED, Priority.HIGH);
        tasks = List.of(task1, task2, task3);
    }

    @Test
    public void getTasksTest() {
        Mockito.when(taskRepository.findAll()).thenReturn(tasks);
        Mockito.when(executorsTaskRepository.countExecutors(any())).thenReturn(0);
        taskService.setTaskMapper(taskMapper);
        List<TaskDTO> taskDTOs = tasks.stream().map(o -> taskMapper.fromEntity(o)).collect(Collectors.toList());

        List<TaskDTO> testTasks = taskService.getTasks();

        Assertions.assertIterableEquals(taskDTOs, testTasks);
    }

    @Test
    public void getTaskByIdTest() {
        Mockito.when(executorsTaskRepository.findByTaskId(UUID.nameUUIDFromBytes(bytes))).thenReturn(List.of(executorsTasks));
        AccountDTO accountDTO = new AccountDTO(UUID.nameUUIDFromBytes(bytes), "test");
        Mockito.when(taskService.getAccount(anyString(), any(UUID.class))).thenReturn(accountDTO);
        Mockito.when(taskRepository.findById(UUID.nameUUIDFromBytes(bytes)))
                .thenReturn(tasks.stream().filter(o -> o.getId().equals(UUID.nameUUIDFromBytes(bytes))).findFirst());
        taskService.setTaskMapper(taskMapper);

        TaskWithExecutorsDTO testTask = taskService.getTaskById((UUID.nameUUIDFromBytes(bytes).toString()), "token");
        TaskWithExecutorsDTO task = taskMapper.fromEntityForExecutorsTask(tasks.get(0));
        task.setExecutors(Set.of(accountDTO));

        Assertions.assertEquals(task, testTask);
    }

    @Test
    public void getTaskByIdTest_ThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskService.getTaskById("1", "token"));
    }

    @Test
    public void getTaskByAccountIdTest() {
        taskService.setTaskMapper(taskMapper);
        UUID authorId = UUID.nameUUIDFromBytes(bytes);
        Mockito.when(taskRepository.findByAuthorId(authorId))
                .thenReturn(tasks.stream().filter(o -> o.getAuthorId().equals(authorId)).collect(Collectors.toList()));

        List<TaskWithExecutorsDTO> testTasks = taskService.getTasksByAccountId("token", String.valueOf(authorId));
        TaskWithExecutorsDTO expectedTasks = taskMapper.fromEntityForExecutorsTask(tasks.get(1));
        expectedTasks.setExecutors(new HashSet<>());

        Assertions.assertEquals(List.of(expectedTasks), testTasks);
    }
}
