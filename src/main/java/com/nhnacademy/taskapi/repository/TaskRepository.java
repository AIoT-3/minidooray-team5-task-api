package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select t from Task t where t.project.id=:projectId")
    List<Task> findAllByProject_Id(Long projectId);
}
