package com.example.taskmanager.services;


import com.example.taskmanager.entities.Task;
import com.example.taskmanager.repositories.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();

    }

    public Task getTaskById(Long id) {

        Optional<Task> task = taskRepository.findById(id);

        return task.orElse(null);

    }

    public List<Task> getTasksToDo(){
        return taskRepository.findTasksToDo();
    }

    public ResponseEntity<String> createNewTask(Task task) {

            taskRepository.save(task);
            return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> editTaskStatus(Long id) {
        Optional<Task> fetchedTask = taskRepository.findById(id);

        if (fetchedTask.isPresent()) {
            taskRepository.updateTaskStatus(id);
            return new ResponseEntity<>("Status edited successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Task not found", HttpStatus.BAD_REQUEST);
        }
    }
}
