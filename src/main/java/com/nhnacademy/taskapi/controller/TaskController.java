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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;

    // 프로젝트 내 모든 테스크 조회
    @GetMapping
    public ResponseEntity<List<TaskSummaryResponse>> getTasksByProject(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("X-User-Id") String userId
    ) {
        List<TaskSummaryResponse> resp=taskService.getTasks(projectId, userId);

        return ResponseEntity.ok(resp);
    }

    // 테스크 생성
    @PostMapping
    public ResponseEntity<TaskDetailResponse> createTask(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody TaskCreateRequest req
    ) {
        TaskDetailResponse resp=taskService.createTask(projectId, userId, req);

        return ResponseEntity.ok(resp);
    }

    // 테스크 단건 조회
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDetailResponse> getTask(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @RequestHeader("X-User-Id") String userId
    ) {
        TaskDetailResponse resp=taskService.getTask(projectId, taskId, userId);

        return ResponseEntity.ok(resp);
    }

    // 테스크 수정
    @PostMapping("/{taskId}")
    public ResponseEntity<TaskDetailResponse> updateTask(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody TaskUpdateRequest req
            ) {
        TaskDetailResponse resp=taskService.updateTask(projectId, userId, taskId, req);

        return ResponseEntity.ok(resp);
    }

    // 테스크 삭제
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId,
            @RequestHeader("X-User-Id") String userId
    ) {
        taskService.deleteTask(projectId, userId, taskId);

        return ResponseEntity.noContent().build();
    }
}
