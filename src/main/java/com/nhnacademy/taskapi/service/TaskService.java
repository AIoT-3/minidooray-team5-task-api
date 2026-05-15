package com.nhnacademy.taskapi.service;

import com.nhnacademy.taskapi.dto.task.TaskCreateRequest;
import com.nhnacademy.taskapi.dto.task.TaskDetailResponse;
import com.nhnacademy.taskapi.dto.task.TaskSummaryResponse;
import com.nhnacademy.taskapi.dto.task.TaskUpdateRequest;

import java.util.List;

public interface TaskService {
    List<TaskSummaryResponse> getTasks(Long projectId, String userId);

    TaskDetailResponse createTask(Long projectId, String userId, TaskCreateRequest req);

    TaskDetailResponse getTask(Long projectId, Long taskId, String userId);

    TaskDetailResponse updateTask(Long projectId, String userId, Long taskId, TaskUpdateRequest req);

    void deleteTask(Long projectId, String userId, Long taskId);
}
