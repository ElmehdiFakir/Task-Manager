package com.example.taskmanager.controllers;

import com.example.taskmanager.entities.Task;
import com.example.taskmanager.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<Task>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Task> tasks = taskService.getAllTasks(pageable);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {

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
