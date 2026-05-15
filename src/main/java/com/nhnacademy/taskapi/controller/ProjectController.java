package com.nhnacademy.taskapi.controller;

import com.nhnacademy.taskapi.dto.project.ProjectCreateRequest;
import com.nhnacademy.taskapi.dto.project.ProjectResponse;
import com.nhnacademy.taskapi.dto.project.ProjectUpdateRequest;
import com.nhnacademy.taskapi.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    // 프로젝트 목록 조회
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects(
            @RequestHeader("X-User-Id") String userId
    ) {
        List<ProjectResponse> projects=projectService.getProjects(userId);

        return ResponseEntity.ok(projects);
    }

    // 프로젝트 생성
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectCreateRequest req,
            @RequestHeader("X-User-Id") String userId
    ) {
        ProjectResponse resp=projectService.createProject(req, userId);

        return ResponseEntity.ok(resp);
    }

    // 프로젝트 단건 조회
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(
            @PathVariable Long projectId,
            @RequestHeader("X-User-Id") String userId
    ) {
        ProjectResponse resp=projectService.getProject(projectId, userId);

        return ResponseEntity.ok(resp);
    }

    // 프로젝트 수정
    @PostMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long projectId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody ProjectUpdateRequest req
    ) {
        ProjectResponse resp=projectService.updateProject(projectId, req, userId);

        return ResponseEntity.ok(resp);
    }

    // 프로젝트 삭제
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long projectId,
            @RequestHeader("X-User-Id") String userId
    ) {
        projectService.deleteProject(projectId, userId);

        return ResponseEntity.noContent().build();
    }
}
