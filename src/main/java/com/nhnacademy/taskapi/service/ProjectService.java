package com.nhnacademy.taskapi.service;

import com.nhnacademy.taskapi.dto.project.ProjectCreateRequest;
import com.nhnacademy.taskapi.dto.project.ProjectResponse;
import com.nhnacademy.taskapi.dto.project.ProjectUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProjectService {
    ProjectResponse createProject(ProjectCreateRequest req, String adminId);

    ProjectResponse getProject(Long projectId, String userId);

    List<ProjectResponse> getProjects(String userId);

    ProjectResponse updateProject(Long projectId, ProjectUpdateRequest req, String userId);

    void deleteProject(Long projectId, String userId);
}
