package com.nhnacademy.taskapi.controller;

import com.nhnacademy.taskapi.dto.task.TaskCreateRequest;
import com.nhnacademy.taskapi.dto.task.TaskDetailResponse;
import com.nhnacademy.taskapi.dto.task.TaskSummaryResponse;
import com.nhnacademy.taskapi.dto.task.TaskUpdateRequest;
import com.nhnacademy.taskapi.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<TaskSummaryResponse> getTasksByProject(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("X-User-Id") String userId
    ) {

        return null;
    }

    @PostMapping
    public ResponseEntity<TaskDetailResponse> createTask(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody TaskCreateRequest req
            ) {

        return null;
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDetailResponse> getTask(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @RequestHeader("X-User-Id") String userId
    ) {

        return null;
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<TaskDetailResponse> updateTask(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody TaskUpdateRequest req
            ) {

        return null;
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @RequestHeader("X-User-Id") String userId
    ) {

        return null;
    }
}
