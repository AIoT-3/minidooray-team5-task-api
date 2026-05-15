package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByProject_Id(Long projectId);
}
