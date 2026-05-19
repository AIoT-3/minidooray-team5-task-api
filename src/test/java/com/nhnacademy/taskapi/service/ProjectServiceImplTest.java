package com.nhnacademy.taskapi.service;

import com.nhnacademy.taskapi.dto.project.ProjectCreateRequest;
import com.nhnacademy.taskapi.dto.project.ProjectResponse;
import com.nhnacademy.taskapi.dto.project.ProjectUpdateRequest;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.ProjectMember;
import com.nhnacademy.taskapi.entity.ProjectStatus;
import com.nhnacademy.taskapi.exception.allow.ex.ProjectNotAllowException;
import com.nhnacademy.taskapi.exception.notfound.ex.ProjectMemberNotFoundException;
import com.nhnacademy.taskapi.exception.notfound.ex.ProjectNotFoundException;
import com.nhnacademy.taskapi.repository.*;
import com.nhnacademy.taskapi.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskTagRepository taskTagRepository;

    @Mock
    private CommentRepository commentRepository;

    private final String userId="user1";
    private final Long projectId=1L;

    @Nested
    @DisplayName("프로젝트 생성")
    class createProject {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            ProjectCreateRequest req=new ProjectCreateRequest("project1");
            Project project=Project.builder()
                    .name(req.name())
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            // when
            when(projectRepository.save(any(Project.class))).thenReturn(project);
            ProjectResponse resp=projectService.createProject(req, userId);

            // then
            assertAll(
                    () -> assertEquals(projectId, resp.projectId()),
                    () -> assertEquals(req.name(), resp.name()),
                    () -> assertTrue(resp.admin())
            );
        }
    }

    @Nested
    @DisplayName("프로젝트 단건 조회")
    class getProject {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Project project = Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            ProjectMember projectMember = ProjectMember.builder()
                    .userId(userId)
                    .admin(true)
                    .build();

            // when
            when(projectRepository.findById(projectId)).thenReturn(Optional.ofNullable(project));
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, userId)).thenReturn(Optional.ofNullable(projectMember));
            ProjectResponse resp = projectService.getProject(projectId, userId);

            // then
            assertAll(
                    () -> assertEquals(projectId, resp.projectId()),
                    () -> assertEquals(project.getName(), resp.name()),
                    () -> assertTrue(resp.admin())
            );
        }

        @Test
        @DisplayName("실패 - 프로젝트 없음")
        void fail_projectNotFound() {
            // when
            when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

            // then
            assertThrows(ProjectNotFoundException.class, () -> projectService.getProject(projectId, userId));
        }

        @Test
        @DisplayName("실패 - 프로젝트 멤버 없음")
        void fail_projectMemberNotFound() {
            // given
            Project project = Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            // when
            when(projectRepository.findById(projectId)).thenReturn(Optional.ofNullable(project));
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, userId)).thenReturn(Optional.empty());

            // then
            assertThrows(ProjectMemberNotFoundException.class, () -> projectService.getProject(projectId, userId));
        }
    }

    @Nested
    @DisplayName("프로젝트 목록 조회")
    class getProjects {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Project project1 = Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project1, "id", 1L);

            Project project2 = Project.builder()
                    .name("project2")
                    .build();
            ReflectionTestUtils.setField(project2, "id", 2L);

            // when
            when(projectRepository.findProjectResponsesByUserId(userId)).thenReturn(
                    List.of(
                            ProjectResponse.builder()
                                    .projectId(project1.getId())
                                    .name(project1.getName())
                                    .admin(true)
                                    .build(),
                            ProjectResponse.builder()
                                    .projectId(project2.getId())
                                    .name(project2.getName())
                                    .admin(false)
                                    .build()
                    )
            );
            List<ProjectResponse> resp = projectService.getProjects(userId);

            // then
            assertAll(
                    () -> assertEquals(2, resp.size()),
                    () -> assertEquals(project1.getId(), resp.get(0).projectId()),
                    () -> assertEquals(project1.getName(), resp.get(0).name()),
                    () -> assertTrue(resp.get(0).admin()),
                    () -> assertEquals(project2.getId(), resp.get(1).projectId()),
                    () -> assertEquals(project2.getName(), resp.get(1).name()),
                    () -> assertFalse(resp.get(1).admin())
            );
        }
    }

    @Nested
    @DisplayName("프로젝트 수정")
    class updateProject {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Project project = Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            ProjectMember projectMember = ProjectMember.builder()
                    .userId(userId)
                    .admin(true)
                    .build();

            // when
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, userId)).thenReturn(Optional.ofNullable(projectMember));
            when(projectRepository.findById(projectId)).thenReturn(Optional.ofNullable(project));
            ProjectResponse resp = projectService.updateProject(projectId, new ProjectUpdateRequest("project1_updated", ProjectStatus.ACTIVE), userId);

            // then
            assertAll(
                    () -> assertEquals(projectId, resp.projectId()),
                    () -> assertEquals("project1_updated", resp.name()),
                    () -> assertTrue(resp.admin())
            );
        }

        @Test
        @DisplayName("실패 - 프로젝트 멤버 없음")
        void fail_projectMemberNotFound() {
            // when
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, userId)).thenReturn(Optional.empty());

            // then
            assertThrows(ProjectMemberNotFoundException.class, () -> projectService.updateProject(projectId, new ProjectUpdateRequest("project1_updated", ProjectStatus.ACTIVE), userId));
        }

        @Test
        @DisplayName("실패 - 프로젝트 멤버 권한 없음")
        void fail_projectMemberNotAllow() {
            // given
            ProjectMember projectMember = ProjectMember.builder()
                    .userId(userId)
                    .admin(false)
                    .build();

            // when
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, userId)).thenReturn(Optional.ofNullable(projectMember));

            // then
            assertThrows(ProjectNotAllowException.class, () -> projectService.updateProject(projectId, new ProjectUpdateRequest("project1_updated", ProjectStatus.ACTIVE), userId));
        }

        @Test
        @DisplayName("실패 - 프로젝트 없음")
        void fail_projectNotFound() {
            // given
            ProjectMember projectMember = ProjectMember.builder()
                    .userId(userId)
                    .admin(true)
                    .build();

            // when
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, userId)).thenReturn(Optional.ofNullable(projectMember));
            when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

            // then
            assertThrows(ProjectNotFoundException.class, () -> projectService.updateProject(projectId, new ProjectUpdateRequest("project1_updated", ProjectStatus.ACTIVE), userId));
        }
    }

    @Nested
    @DisplayName("프로젝트 삭제")
    class deleteProject {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            ProjectMember projectMember = ProjectMember.builder()
                    .userId(userId)
                    .admin(true)
                    .build();

            Project project = Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            // when
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, userId)).thenReturn(Optional.ofNullable(projectMember));
            when(projectRepository.findById(projectId)).thenReturn(Optional.ofNullable(project));
            assertDoesNotThrow(() -> projectService.deleteProject(projectId, userId));
        }

        @Test
        @DisplayName("실패 - 프로젝트 멤버 없음")
        void fail_projectMemberNotFound() {
            // when
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, userId)).thenReturn(Optional.empty());

            // then
            assertThrows(ProjectMemberNotFoundException.class, () -> projectService.deleteProject(projectId, userId));
        }
    }
}
