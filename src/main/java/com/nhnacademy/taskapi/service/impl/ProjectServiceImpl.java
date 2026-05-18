package com.nhnacademy.taskapi.service.impl;

import com.nhnacademy.taskapi.dto.project.ProjectCreateRequest;
import com.nhnacademy.taskapi.dto.project.ProjectResponse;
import com.nhnacademy.taskapi.dto.project.ProjectUpdateRequest;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.ProjectMember;
import com.nhnacademy.taskapi.exception.notfound.ex.ProjectMemberNotFoundException;
import com.nhnacademy.taskapi.exception.allow.ex.ProjectNotAllowException;
import com.nhnacademy.taskapi.exception.notfound.ex.ProjectNotFoundException;
import com.nhnacademy.taskapi.repository.*;
import com.nhnacademy.taskapi.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TagRepository tagRepository;
    private final MilestoneRepository milestoneRepository;
    private final TaskRepository taskRepository;
    private final TaskTagRepository taskTagRepository;
    private final CommentRepository commentRepository;

    // 프로젝트 생성
    @Override
    @Transactional
    public ProjectResponse createProject(ProjectCreateRequest req, String adminId) {

        // 프로젝트 생성
        Project project=Project.builder()
                .name(req.name())
                .build();

        // 프로젝트 멤버 생성
        ProjectMember projectMember=ProjectMember.builder()
                .userId(adminId)
                .admin(true)
                .build();

        // 연관관계 설정 -> project에 멤버 추가
        project.addMember(projectMember);

        // 프로젝트 저장
        Project savedProject=projectRepository.save(project);

        // 응답 반환
        return ProjectResponse.builder()
                .projectId(savedProject.getId())
                .name(savedProject.getName())
                .status(project.getStatus())
                .admin(projectMember.isAdmin())
                .build();
    }

    // 프로젝트 단건 조회
    @Override
    public ProjectResponse getProject(Long projectId, String userId) {
        // 프로젝트 조회
        Project project=projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException(projectId+"번 프로젝트를 찾을 수 없습니다."));

        // 프로젝트 멤버 조회 -> 프로젝트 참여 여부 확인
        ProjectMember projectMember=projectMemberRepository.findByProject_IdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectMemberNotFoundException(projectId+"번 프로젝트는" + userId + "님이 참여하고 있지 않습니다."));

        // 응답 반환
        return ProjectResponse.builder()
                .projectId(project.getId())
                .name(project.getName())
                .status(project.getStatus())
                .admin(projectMember.isAdmin())
                .build();
    }

    // 프로젝트 목록 조회
    @Override
    public List<ProjectResponse> getProjects(String userId) {
        // 프로젝트 멤버 조회 -> 프로젝트 참여 여부 확인
        return projectRepository.findProjectResponsesByUserId(userId);
    }

    // 프로젝트 수정
    @Override
    @Transactional
    public ProjectResponse updateProject(Long projectId, ProjectUpdateRequest req, String userId) {
        // 프로젝트 멤버 조회 -> 프로젝트 참여 여부 확인
        ProjectMember projectMember=projectMemberRepository.findByProject_IdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectMemberNotFoundException(projectId+"번 프로젝트는" + userId + "님이 참여하고 있지 않습니다."));

        // 프로젝트 멤버 권한 확인
        if(!projectMember.isAdmin()) {
            throw new ProjectNotAllowException(projectId+"번 프로젝트는" + userId + "님이 관리자 권한이 없습니다.");
        }

        // 프로젝트 조회
        Project project=projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId+"번 프로젝트를 찾을 수 없습니다."));

        // 프로젝트 수정
        project.update(req.name(), req.status());

        // 응답 반환
        return ProjectResponse.builder()
                .projectId(project.getId())
                .name(project.getName())
                .status(project.getStatus())
                .admin(projectMember.isAdmin())
                .build();
    }

    // 프로젝트 삭제
    @Override
    @Transactional
    public void deleteProject(Long projectId, String userId) {
        // 프로젝트 멤버 조회 -> 프로젝트 차며 여부 확인
        ProjectMember projectMember=projectMemberRepository.findByProject_IdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectMemberNotFoundException(projectId+"번 프로젝트는" + userId + "님이 참여하고 있지 않습니다."));

        // 프로젝트 멤버 권한 확인
        if(!projectMember.isAdmin()) {
            throw new ProjectNotAllowException(projectId+"번 프로젝트는" + userId + "님이 관리자 권한이 없습니다.");
        }

        // 프로젝트 조회
        Project project=projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId+"번 프로젝트를 찾을 수 없습니다."));

        // 프로젝트완 연관된 프로젝트 멤버 삭제
        taskTagRepository.deleteAllByProjectIdInBulk(projectId);

        // 프로젝트완 연관된 댓글 삭제
        commentRepository.deleteAllByProjectIdInBulk(projectId);

        // 프로젝트완 연관된 테스크 삭제
        taskRepository.deleteByProject_Id(projectId);

        // 프로젝트완 연관된 태그 삭제
        tagRepository.deleteByProject_Id(projectId);

        // 프로젝트완 연관된 마일스톤 삭제
        milestoneRepository.deleteByProject_Id(projectId);

        // 프로젝트 삭제 -> 연관된 프로젝트 멤버도 함께 삭제
        projectRepository.delete(project);
    }
}
