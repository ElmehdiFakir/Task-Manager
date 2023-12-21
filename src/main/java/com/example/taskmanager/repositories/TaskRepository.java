package com.example.taskmanager.repositories;

import com.example.taskmanager.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

    @Query(value = "SELECT t FROM Task t WHERE t.completed = false")
    List<Task> findTasksToDo();

    @Modifying(clearAutomatically = true)
    @Query("update Task t set t.completed = true where t.id = :id")
    void updateTaskStatus(@Param("id") Long id);
}
