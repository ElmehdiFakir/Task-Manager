package com.example.taskmanager.services;import com.example.taskmanager.entities.Task;
import com.example.taskmanager.repositories.TaskRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTasks_ReturnsListOfTasks() {
        // Arrange
        List<Task> tasks = Arrays.asList(new Task(1L, "Create user stories", false),
                new Task(1L, "Development of US", false));

        // Créez une Page<Task> simulée pour représenter la pagination
        Page<Task> taskPage = new PageImpl<>(tasks);

        when(taskRepository.findAll(any(Pageable.class))).thenReturn(taskPage);

        // Act
        Page<Task> result = taskService.getAllTasks(PageRequest.of(0, 10));

        // Assert
        assertEquals(tasks.size(), result.getContent().size());
    }

    @Test
    void getTaskById_ExistingId_ReturnsTask(){
        // Arrange
        Long taskId = 1L;
        Task task = new Task(1L, "Task 1", false);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act
        Task result = taskService.getTaskById(taskId);

        // Assert
        assertNotNull(result);
        assertEquals(task, result);
    }

    @Test
    void getTaskById_NonExistingId_ReturnsNull() {
        // Arrange
        Long taskId = 999L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act
        Task result = taskService.getTaskById(taskId);

        // Assert
        assertNull(result);
    }

    @Test
    void getTasksToDo_ReturnsListOfTasks() {
        // Arrange
        List<Task> tasksToDo = Arrays.asList(new Task(1L, "Create user stories", false),
                new Task(1L, "Development of US", false));

        when(taskRepository.findTasksToDo()).thenReturn(tasksToDo);

        // Act
        List<Task> result = taskService.getTasksToDo();

        // Assert
        assertEquals(tasksToDo.size(), result.size());
        assertTrue(result.containsAll(tasksToDo));
    }

    @Test
    void createNewTask_ValidTask_ReturnsOkStatus() {
        // Arrange
        Task newTask = new Task(1L, "Create user stories", false);

        // Simulez le comportement de la méthode save pour générer une exception
        doThrow(new RuntimeException("Failed to save")).when(taskRepository).save(newTask);

        // Act
        ResponseEntity<String> result = taskService.createNewTask(newTask);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void editTaskStatus_ExistingId_ReturnsOkStatus() {
        // Arrange
        Long taskId = 1L;
        Task existingTask = new Task(1L, "Create user stories", false);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        // Act
        ResponseEntity<String> result = taskService.editTaskStatus(taskId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void editTaskStatus_NonExistingId_ReturnsBadRequestStatus() {
        // Arrange
        Long nonExistingTaskId = 999L;

        when(taskRepository.findById(nonExistingTaskId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> result = taskService.editTaskStatus(nonExistingTaskId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}
