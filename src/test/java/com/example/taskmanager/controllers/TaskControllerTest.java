package com.example.taskmanager.controllers;

import com.example.taskmanager.entities.Task;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTasks_ReturnsListOfTasks() {
        // Arrange
        Task task1 = new Task(1L, "Task 1", false);
        Task task2 = new Task(2L, "Task 2", true);
        List<Task> tasks = Arrays.asList(task1, task2);

        // Créez une Page<Task> simulée pour représenter la pagination
        Page<Task> taskPage = new PageImpl<>(tasks);
        Pageable pageable = PageRequest.of(0, 2);

        when(taskRepository.findAll(pageable)).thenReturn(taskPage);

        // Act
        ResponseEntity<Page<Task>> responseEntity = taskController.getAllTasks(0, 2);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void addNewTask_ReturnsOkStatus() {
        // Arrange
        Task newTask = new Task(1L, "Create User Stories", false);

        // Assuming createNewTask returns a ResponseEntity<String>
        when(taskService.createNewTask(newTask)).thenReturn(new ResponseEntity<>("Task created successfully", HttpStatus.OK));

        // Act
        ResponseEntity<String> responseEntity = taskController.addNewTask(newTask);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Task created successfully", responseEntity.getBody());
    }

    @Test
    void getTaskById_ExistingId_ReturnsTask() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task(1L, "Task To Do", false);

        when(taskService.getTaskById(taskId)).thenReturn(task);

        // Act
        ResponseEntity<?> responseEntity = taskController.getTaskById(taskId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(task, responseEntity.getBody());
    }

    @Test
    void getTaskById_NonExistingId_ReturnsNotFoundStatus() {
        // Arrange
        Long taskId = 999L;

        when(taskService.getTaskById(taskId)).thenReturn(null);

        // Act
        ResponseEntity<?> responseEntity = taskController.getTaskById(taskId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getTasksToDo_ReturnsListOfTasks() {
        // Arrange
        Task task1 = new Task(1L, "Task 1", false);
        Task task3 = new Task(1L, "Task 3", false);
        List<Task> tasks = Arrays.asList(task1, task3);

        when(taskService.getTasksToDo()).thenReturn(tasks);

        // Act
        ResponseEntity<List<Task>> responseEntity = taskController.getTaskById();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(tasks, responseEntity.getBody());
    }

    @Test
    void editTaskStatus_ExistingId_ReturnsOkStatus() {
        // Arrange
        Long taskId = 1L;

        // Assuming editTaskStatus returns a ResponseEntity<?>
        when(taskService.editTaskStatus(taskId)).thenReturn(new ResponseEntity<>("Task status edited successfully", HttpStatus.OK));

        // Act
        ResponseEntity<?> responseEntity = taskController.editTaskStatus(taskId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Task status edited successfully", responseEntity.getBody());
    }

    @Test
    void editTaskStatus_NonExistingId_ReturnsNotFoundStatus() {
        // Arrange
        Long taskId = 999L;

        // Assuming editTaskStatus returns a ResponseEntity<?>
        when(taskService.editTaskStatus(taskId)).thenReturn(new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND));

        // Act
        ResponseEntity<?> responseEntity = taskController.editTaskStatus(taskId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Task not found", responseEntity.getBody());
    }
}
