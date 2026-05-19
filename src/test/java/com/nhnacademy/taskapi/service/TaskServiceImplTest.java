package com.nhnacademy.taskapi.service;

import com.nhnacademy.taskapi.dto.task.TaskCreateRequest;
import com.nhnacademy.taskapi.dto.task.TaskDetailResponse;
import com.nhnacademy.taskapi.dto.task.TaskSummaryResponse;
import com.nhnacademy.taskapi.dto.task.TaskUpdateRequest;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.Task;
import com.nhnacademy.taskapi.exception.notfound.ex.ProjectNotFoundException;
import com.nhnacademy.taskapi.repository.*;
import com.nhnacademy.taskapi.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskTagRepository taskTagRepository;

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private CommentRepository commentRepository;

    @Nested
    @DisplayName("테스크 생성")
    class CreateTaskTest {
        @Test
        @DisplayName("테스크 생성 - 성공")
        public void test_createTask_success() {
            // given
            Long projectId=1L;
            String userId="taskUser1";
            TaskCreateRequest req=new TaskCreateRequest("test title", "test content", 1L);
            Project project=Project.builder()
                    .name("test project")
                    .build();
            Task task=Task.builder()
                    .title("test title")
                    .content("test content")
                    .userId("testUser1")
                    .project(project)
                    .build();

            // when
            when(projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)).thenReturn(true);
            when(projectRepository.findById(projectId)).thenReturn(Optional.ofNullable(project));
            when(taskRepository.save(any())).thenReturn(task);
            TaskDetailResponse resp=taskService.createTask(projectId, userId, req);

            // then
            assertAll(
                    ()->assertEquals(req.title(), resp.title()),
                    ()->assertEquals(req.content(), resp.content()),
                    ()->assertEquals(req.projectId(), resp.projectId())
            );
        }

        @Test
        @DisplayName("테스트 생성 - 프로젝트 존재 X")
        public void test_createTask_projectNotFound() {
            // given
            Long projectId=1L;
            String userId="taskUser1";
            TaskCreateRequest req=new TaskCreateRequest("test title", "test content", 1L);

            // when
            when(projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)).thenReturn(true);
            when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

            // then
            assertThrows(ProjectNotFoundException.class,
                    () -> taskService.createTask(projectId, userId, req));
        }
    }

    @Nested
    @DisplayName("테스크 리스트 조회")
    class GetTaskListTest {
        @Test
        @DisplayName("테스크 리스트 조회 - 성공")
        public void test_getTasks_success() {
            // given
            Long projectId=1L;
            String userId="taskUser";

            // when
            when(projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)).thenReturn(true);
            when(taskRepository.findAllByProject_Id(projectId)).thenReturn(
                    List.of(
                            Task.builder()
                                    .title("test title1")
                                    .content("test content1")
                                    .userId("taskUser")
                                    .build(),
                            Task.builder()
                                    .title("test title2")
                                    .content("test content2")
                                    .userId("taskUser")
                                    .build()
                    )
            );
            List<TaskSummaryResponse> resp=taskService.getTasks(projectId, userId);

            // then
            assertAll(
                    ()->assertEquals(2, resp.size()),
                    ()->assertEquals("test title1", resp.get(0).title()),
                    ()->assertEquals("test title2", resp.get(1).title())
            );
        }
    }

    @Nested
    @DisplayName("테스크 상세 조회")
    class GetTaskDetailTest {
        @Test
        @DisplayName("테스크 상세 조회 - 성공")
        public void test_getTask_success() {
            // given
            Long projectId=1L;
            String userId="taskUser";
            Long taskId=1L;
            Project project=Project.builder()
                    .name("test project")
                    .build();


            // when
            when(projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)).thenReturn(true);
            when(taskRepository.findById(taskId)).thenReturn(
                    Optional.ofNullable(
                            Task.builder()
                                    .title("test title")
                                    .content("test content")
                                    .userId("taskUser")
                                    .project(project)
                                    .build()
                    )
            );
            TaskDetailResponse resp=taskService.getTask(projectId, taskId, userId);

            // then
            assertAll(
                    ()->assertEquals("test title", resp.title()),
                    ()->assertEquals("test content", resp.content())
            );
        }

        @Test
        @DisplayName("테스크 상세 조회 - 테스크 존재 X")
        public void test_getTask_taskNotFound() {
            // given
            Long projectId=1L;
            String userId="taskUser";
            Long taskId=1L;

            // when
            when(projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)).thenReturn(true);
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

            // then
            assertThrows(com.nhnacademy.taskapi.exception.notfound.ex.TaskNotFoundException.class,
                    () -> taskService.getTask(projectId, taskId, userId));
        }
    }

    @Nested
    @DisplayName("테스크 업데이트")
    class UpdateTaskTest {
        @Test
        @DisplayName("테스크 업데이트 - 성공")
        public void test_updateTask_success() {
            // given
            Long projectId=1L;
            String userId="taskUser";
            Long taskId=1L;
            Project project=Project.builder()
                    .name("test project")
                    .build();

            // when
            when(projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)).thenReturn(true);
            when(taskRepository.findById(taskId)).thenReturn(
                    Optional.ofNullable(
                            Task.builder()
                                    .title("test title")
                                    .content("test content")
                                    .userId("taskUser")
                                    .project(project)
                                    .build()
                    )
            );
            TaskDetailResponse resp=taskService.updateTask(projectId, userId, taskId, new TaskUpdateRequest("updated title", "updated content", null, List.of()));

            // then
            assertAll(
                    ()->assertEquals("updated title", resp.title()),
                    ()->assertEquals("updated content", resp.content())
            );
        }

        @Test
        @DisplayName("테스크 업데이트 - 테스크 존재 X")
        public void test_updateTask_taskNotFound() {
            // given
            Long projectId=1L;
            String userId="taskUser";
            Long taskId=1L;

            // when
            when(projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)).thenReturn(true);
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

            // then
            assertThrows(com.nhnacademy.taskapi.exception.notfound.ex.TaskNotFoundException.class,
                    () -> taskService.updateTask(projectId, userId, taskId, new TaskUpdateRequest("updated title", "updated content", null, List.of())));
        }
    }

    @Nested
    @DisplayName("테스크 삭제")
    class DeleteTaskTest {
        @Test
        @DisplayName("테스크 삭제 - 성공")
        public void test_deleteTask_success() {
            // given
            Long projectId=1L;
            String userId="taskUser";
            Long taskId=1L;
            Project project=Project.builder()
                    .name("test project")
                    .build();

            // when
            when(projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)).thenReturn(true);
            when(taskRepository.findById(taskId)).thenReturn(
                    Optional.ofNullable(
                            Task.builder()
                                    .title("test title")
                                    .content("test content")
                                    .userId("taskUser")
                                    .project(project)
                                    .build()
                    )
            );
            assertDoesNotThrow(() -> taskService.deleteTask(projectId, userId, taskId));
        }

        @Test
        @DisplayName("테스크 삭제 - 테스크 존재 X")
        public void test_deleteTask_taskNotFound() {
            // given
            Long projectId=1L;
            String userId="taskUser";
            Long taskId=1L;

            // when
            when(projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)).thenReturn(true);
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

            // then
            assertThrows(com.nhnacademy.taskapi.exception.notfound.ex.TaskNotFoundException.class,
                    () -> taskService.deleteTask(projectId, userId, taskId));
        }
    }
}
