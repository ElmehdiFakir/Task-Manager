package com.example.taskmanager.controllers;

import com.example.taskmanager.entities.Task;
import com.example.taskmanager.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {

        List<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getTaskById(@PathVariable Long id) {

        Task task = taskService.getTaskById(id);

        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/todo")
    public ResponseEntity<List<Task>> getTaskById() {

        List<Task> tasks = taskService.getTasksToDo();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<String> addNewTask(@RequestBody Task task) {
        return taskService.createNewTask(task);
    }

    @PutMapping("/{id}/status/edit")
    public ResponseEntity<?> editTaskStatus(@PathVariable("id") Long id) {
        return taskService.editTaskStatus(id);
    }

}
