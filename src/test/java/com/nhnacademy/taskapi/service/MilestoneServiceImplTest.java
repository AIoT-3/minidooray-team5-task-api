package com.nhnacademy.taskapi.service;

import com.nhnacademy.taskapi.dto.milestone.MilestoneCreateRequest;
import com.nhnacademy.taskapi.dto.milestone.MilestoneResponse;
import com.nhnacademy.taskapi.dto.milestone.MilestoneUpdateRequest;
import com.nhnacademy.taskapi.entity.Milestone;
import com.nhnacademy.taskapi.entity.MilestoneStatus;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.exception.allow.ex.MilestoneNotAllowException;
import com.nhnacademy.taskapi.exception.notfound.ex.MilestoneNotFoundException;
import com.nhnacademy.taskapi.exception.notfound.ex.ProjectNotFoundException;
import com.nhnacademy.taskapi.repository.MilestoneRepository;
import com.nhnacademy.taskapi.repository.ProjectMemberRepository;
import com.nhnacademy.taskapi.repository.ProjectRepository;
import com.nhnacademy.taskapi.repository.TaskRepository;
import com.nhnacademy.taskapi.service.impl.MilestoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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
public class MilestoneServiceImplTest {

    @InjectMocks
    private MilestoneServiceImpl milestoneService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    private final Long projectId=1L;
    private final String userId="user1";

    @BeforeEach
    void setup() {
        when(projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)).thenReturn(true);
    }

    @Nested
    @DisplayName("마일스톤 목록 조회")
    class getMilestoneList {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Project project=Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            Milestone milestone1=Milestone.builder()
                    .name("milestone1")
                    .status(MilestoneStatus.OPEN)
                    .project(project)
                    .build();
            ReflectionTestUtils.setField(milestone1, "id", 1L);

            Milestone milestone2=Milestone.builder()
                    .name("milestone2")
                    .status(MilestoneStatus.OPEN)
                    .project(project)
                    .build();
            ReflectionTestUtils.setField(milestone2, "id", 2L);

            // when
            when(milestoneRepository.findAllByProject_Id(projectId)).thenReturn(
                    List.of(milestone1, milestone2)
            );
            List<MilestoneResponse> resp=milestoneService.getMilestoneList(projectId, userId);

            // then
            assertAll(
                    ()->assertEquals(2, resp.size()),
                    ()->assertEquals(1L, resp.get(0).milestoneId()),
                    ()->assertEquals("milestone1", resp.get(0).name()),
                    ()->assertEquals(MilestoneStatus.OPEN, resp.get(0).status()),
                    ()->assertEquals(2L, resp.get(1).milestoneId()),
                    ()->assertEquals("milestone2", resp.get(1).name()),
                    ()->assertEquals(MilestoneStatus.OPEN, resp.get(1).status())
            );
        }
    }

    @Nested
    @DisplayName("마일스톤 생성")
    class createMilestone {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Project project=Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            Milestone milestone=Milestone.builder()
                    .name("milestone1")
                    .status(MilestoneStatus.OPEN)
                    .project(project)
                    .build();
            ReflectionTestUtils.setField(milestone, "id", 1L);

            // when
            when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
            when(milestoneRepository.save(any(Milestone.class))).thenReturn(milestone);
            MilestoneResponse resp=milestoneService.createMilestone(projectId, userId,
                    new MilestoneCreateRequest("milestone1"));

            // then
            assertAll(
                    ()->assertEquals(1L, resp.milestoneId()),
                    ()->assertEquals("milestone1", resp.name()),
                    ()->assertEquals(MilestoneStatus.OPEN, resp.status())
            );
        }

        @Test
        @DisplayName("실패 - 프로젝트 없음")
        void fail_projectNotFound() {
            // given
            MilestoneCreateRequest req=new MilestoneCreateRequest("milestone1");

            // when
            when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

            // then
            assertThrows(ProjectNotFoundException.class,
                    () ->milestoneService.createMilestone(projectId, userId, req));
        }
    }

    @Nested
    @DisplayName("마일스톤 단건 조회")
    class getMilestone {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Project project=Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            Milestone milestone=Milestone.builder()
                    .name("milestone1")
                    .status(MilestoneStatus.OPEN)
                    .project(project)
                    .build();
            ReflectionTestUtils.setField(milestone, "id", 1L);

            // when
            when(milestoneRepository.findById(1L)).thenReturn(Optional.of(milestone));
            MilestoneResponse resp=milestoneService.getMilestone(projectId, userId, 1L);

            // then
            assertAll(
                    ()->assertEquals(1L, resp.milestoneId()),
                    ()->assertEquals("milestone1", resp.name()),
                    ()->assertEquals(MilestoneStatus.OPEN, resp.status())
            );
        }

        @Test
        @DisplayName("실패 - 마일스톤 없음")
        void fail_milestoneNotFound() {
            // when
            when(milestoneRepository.findById(1L)).thenReturn(Optional.empty());

            // then
            assertThrows(MilestoneNotFoundException.class,
                    () ->milestoneService.getMilestone(projectId, userId, 1L));
        }

        @Test
        @DisplayName("실패 - 마일스톤이 해당 프로젝트에 속해있지 않은 경우")
        void fail_milestoneNotAllow() {
            // given
            Project project=Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", 2L);

            Milestone milestone=Milestone.builder()
                    .name("milestone1")
                    .status(MilestoneStatus.OPEN)
                    .project(project)
                    .build();
            ReflectionTestUtils.setField(milestone, "id", 1L);

            // when
            when(milestoneRepository.findById(1L)).thenReturn(Optional.of(milestone));

            // then
            assertThrows(MilestoneNotAllowException.class,
                    () ->milestoneService.getMilestone(projectId, userId, 1L));
        }
    }

    @Nested
    @DisplayName("마일스톤 수정")
    class updateMilestone {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Project project=Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            Milestone milestone=Milestone.builder()
                    .name("milestone1")
                    .status(MilestoneStatus.OPEN)
                    .project(project)
                    .build();
            ReflectionTestUtils.setField(milestone, "id", 1L);

            // when
            when(milestoneRepository.findById(1L)).thenReturn(Optional.of(milestone));
            MilestoneResponse resp=milestoneService.updateMilestone(projectId, userId, 1L,
                    new MilestoneUpdateRequest("milestone2", MilestoneStatus.IN_PROGRESS));

            // then
            assertAll(
                    ()->assertEquals(1L, resp.milestoneId()),
                    ()->assertEquals("milestone2", resp.name()),
                    ()->assertEquals(MilestoneStatus.IN_PROGRESS, resp.status())
            );
        }

        @Test
        @DisplayName("실패 - 마일스톤 없음")
        void fail_milestoneNotFound() {
            // given
            MilestoneUpdateRequest req=new MilestoneUpdateRequest("milestone2", MilestoneStatus.IN_PROGRESS);

            // when
            when(milestoneRepository.findById(1L)).thenReturn(Optional.empty());

            // then
            assertThrows(MilestoneNotFoundException.class,
                    () ->milestoneService.updateMilestone(projectId, userId, 1L, req));
        }

        @Test
        @DisplayName("실패 - 마일스톤이 해당 프로젝트에 속해있지 않은 경우")
        void fail_milestoneNotAllow() {
            // given
            Project project=Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", 2L);

            Milestone milestone=Milestone.builder()
                    .name("milestone1")
                    .status(MilestoneStatus.OPEN)
                    .project(project)
                    .build();
            ReflectionTestUtils.setField(milestone, "id", 1L);

            MilestoneUpdateRequest req=new MilestoneUpdateRequest("milestone2", MilestoneStatus.IN_PROGRESS);

            // when
            when(milestoneRepository.findById(1L)).thenReturn(Optional.of(milestone));

            // then
            assertThrows(MilestoneNotAllowException.class,
                    () ->milestoneService.updateMilestone(projectId, userId, 1L, req));
        }
    }

    @Nested
    @DisplayName("마일스톤 삭제")
    class deleteMilestone {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Project project=Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            Milestone milestone=Milestone.builder()
                    .name("milestone1")
                    .status(MilestoneStatus.OPEN)
                    .project(project)
                    .build();
            ReflectionTestUtils.setField(milestone, "id", 1L);

            // when
            when(milestoneRepository.findById(1L)).thenReturn(Optional.of(milestone));
            assertDoesNotThrow(() -> milestoneService.deleteMilestone(projectId, userId, 1L));
        }

        @Test
        @DisplayName("실패 - 마일스톤 없음")
        void fail_milestoneNotFound() {
            // when
            when(milestoneRepository.findById(1L)).thenReturn(Optional.empty());

            // then
            assertThrows(MilestoneNotFoundException.class,
                    () ->milestoneService.deleteMilestone(projectId, userId, 1L));
        }

        @Test
        @DisplayName("실패 - 마일스톤이 해당 프로젝트에 속해있지 않은 경우")
        void fail_milestoneNotAllow() {
            // given
            Project project=Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", 2L);

            Milestone milestone=Milestone.builder()
                    .name("milestone1")
                    .status(MilestoneStatus.OPEN)
                    .project(project)
                    .build();
            ReflectionTestUtils.setField(milestone, "id", 1L);

            // when
            when(milestoneRepository.findById(1L)).thenReturn(Optional.of(milestone));

            // then
            assertThrows(MilestoneNotAllowException.class,
                    () ->milestoneService.deleteMilestone(projectId, userId, 1L));
        }
    }
}
