package com.example.taskmanager.repositories;

import com.example.taskmanager.entities.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void findTasksToDo_ReturnsTasksWithIncompleteStatus() {
        // Arrange
        Task task1 = new Task(1L, "Create user stories", true);
        Task task2 = new Task(2L, "Develop the US", false);
        Task task3 = new Task(3L, "Test your development", false);

        taskRepository.saveAll(List.of(task1, task2, task3));

        // Act
        List<Task> tasksToDo = taskRepository.findTasksToDo();

        // Assert
        assertEquals(2, tasksToDo.size());
        assertEquals(tasksToDo.get(0).getId(), 2L);
        assertEquals(tasksToDo.get(1).getId(), 3L);
    }
}
