package com.example.taskmanager.services;


import com.example.taskmanager.entities.Task;
import com.example.taskmanager.repositories.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);

    }

    public Task getTaskById(Long id) {

        Optional<Task> task = taskRepository.findById(id);

        return task.orElse(null);

    }

    public List<Task> getTasksToDo(){
        return taskRepository.findTasksToDo();
    }

    public ResponseEntity<String> createNewTask(Task task) {

        try {
            taskRepository.save(task);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> editTaskStatus(Long id) {
        Optional<Task> fetchedTask = taskRepository.findById(id);

        if (fetchedTask.isPresent()) {
            Task taskToUpdate = fetchedTask.get();
            taskToUpdate.setCompleted(true);
            taskRepository.save(taskToUpdate);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
