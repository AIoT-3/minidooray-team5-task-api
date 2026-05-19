package com.nhnacademy.taskapi.service;

import com.nhnacademy.taskapi.dto.project_member.ProjectMemberResponse;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.ProjectMember;
import com.nhnacademy.taskapi.exception.allow.ex.ProjectNotAllowException;
import com.nhnacademy.taskapi.exception.notfound.ex.ProjectMemberNotFoundException;
import com.nhnacademy.taskapi.exception.notfound.ex.UserNotFoundException;
import com.nhnacademy.taskapi.repository.ProjectMemberRepository;
import com.nhnacademy.taskapi.service.impl.ProjectMemberServiceImpl;
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
public class ProjectMemberServiceImplTest {

    @InjectMocks
    private ProjectMemberServiceImpl projectMemberService;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private AccountClientService accountClientService;

    private final String userId="user1";
    private final Long projectId=1L;

    @Nested
    @DisplayName("프로젝트 멤버 조회")
    class getMembers {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Project project=Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            ProjectMember member1=ProjectMember.builder()
                    .userId("user1")
                    .admin(true)
                    .project(project)
                    .build();

            ProjectMember member2=ProjectMember.builder()
                    .userId("user2")
                    .admin(false)
                    .project(project)
                    .build();

            // when
            when(projectMemberRepository.findAllByProject_Id(projectId)).thenReturn(List.of(member1, member2));
            List<ProjectMemberResponse> resp=projectMemberService.getMembers(projectId, userId);

            // then
            assertAll(
                    ()->assertEquals(2, resp.size()),
                    ()->assertEquals("user1", resp.get(0).userId()),
                    ()->assertTrue(resp.get(0).admin()),
                    ()->assertEquals("user2", resp.get(1).userId()),
                    ()->assertFalse(resp.get(1).admin())
            );
        }

        @Test
        @DisplayName("실패 - 프로젝트 멤버가 아닌 경우")
        void fail_notMember() {
            // given
            Project project=Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            ProjectMember member1=ProjectMember.builder()
                    .userId("user2")
                    .admin(true)
                    .project(project)
                    .build();

            ProjectMember member2=ProjectMember.builder()
                    .userId("user3")
                    .admin(false)
                    .project(project)
                    .build();

            // when
            when(projectMemberRepository.findAllByProject_Id(projectId)).thenReturn(List.of(member1, member2));

            // then
            assertThrows(ProjectNotAllowException.class, ()->projectMemberService.getMembers(projectId, userId));
        }
    }

    @Nested
    @DisplayName("프로젝트 멤버 추가")
    class addMember {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            String newMemberId = "user2";

            Project project = Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            ProjectMember projectMember = ProjectMember.builder()
                    .userId(userId)
                    .admin(true)
                    .project(project)
                    .build();

            ProjectMember newMember = ProjectMember.builder()
                    .userId(newMemberId)
                    .admin(false)
                    .project(project)
                    .build();
            ReflectionTestUtils.setField(newMember, "id", 2L);

            // when
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, userId)).thenReturn(Optional.of(projectMember));
            when(accountClientService.checkUserExists(newMemberId)).thenReturn(true);
            when(projectMemberRepository.save(any(ProjectMember.class))).thenReturn(newMember);

            ProjectMemberResponse resp = projectMemberService.addMember(projectId, userId, newMemberId);

            // then
            assertAll(
                    () -> assertEquals(newMemberId, resp.userId()),
                    () -> assertFalse(resp.admin())
            );
        }

        @Test
        @DisplayName("실패 - 추가하는 유저가 프로젝트 멤버가 아닌 경우")
        void fail_notMember() {
            // when
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, userId)).thenReturn(Optional.empty());

            // then
            assertThrows(ProjectMemberNotFoundException.class, () -> projectMemberService.addMember(projectId, userId, "user2"));
        }

        @Test
        @DisplayName("실패 - 추가하는 유저가 프로젝트 관리자가 아닌 경우")
        void fail_notAdmin() {
            // given
            Project project = Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            ProjectMember projectMember = ProjectMember.builder()
                    .userId(userId)
                    .admin(false)
                    .project(project)
                    .build();

            // when
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, userId)).thenReturn(Optional.of(projectMember));

            // then
            assertThrows(ProjectNotAllowException.class, () -> projectMemberService.addMember(projectId, userId, "user2"));
        }

        @Test
        @DisplayName("실패 - 추가할 멤버가 존재하지 않는 유저인 경우")
        void fail_notExistsUser() {
            // given
            String newMemberId = "user2";

            Project project = Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            ProjectMember projectMember = ProjectMember.builder()
                    .userId(userId)
                    .admin(true)
                    .project(project)
                    .build();

            // when
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, userId)).thenReturn(Optional.of(projectMember));
            when(accountClientService.checkUserExists(newMemberId)).thenReturn(false);

            // then
            assertThrows(UserNotFoundException.class, () -> projectMemberService.addMember(projectId, userId, newMemberId));
        }
    }

    @Nested
    @DisplayName("프로젝트 멤버 삭제")
    class deleteMember {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            String memberId = "user2";

            Project project = Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            ProjectMember projectMember = ProjectMember.builder()
                    .userId(userId)
                    .admin(true)
                    .project(project)
                    .build();

            ProjectMember memberToDelete = ProjectMember.builder()
                    .userId(memberId)
                    .admin(false)
                    .project(project)
                    .build();

            // when
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, userId)).thenReturn(Optional.of(projectMember));
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, memberId)).thenReturn(Optional.of(memberToDelete));

            assertDoesNotThrow(() -> projectMemberService.deleteMember(projectId, userId, memberId));
        }

        @Test
        @DisplayName("실패 - 삭제 요청을 하는 유저가 프로젝트 멤버가 아닌 경우")
        void fail_notMember() {
            // when
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, userId)).thenReturn(Optional.empty());

            // then
            assertThrows(ProjectMemberNotFoundException.class, () -> projectMemberService.deleteMember(projectId, userId, "user2"));
        }

        @Test
        @DisplayName("실패 - 삭제 요청을 하는 유저가 프로젝트 관리자나 본인이 아닌 경우")
        void fail_notAdminOrSelf() {
            // given
            String memberId = "user2";

            Project project = Project.builder()
                    .name("project1")
                    .build();
            ReflectionTestUtils.setField(project, "id", projectId);

            ProjectMember projectMember = ProjectMember.builder()
                    .userId(userId)
                    .admin(false)
                    .project(project)
                    .build();

            // when
            when(projectMemberRepository.findByProject_IdAndUserId(projectId, userId)).thenReturn(Optional.of(projectMember));

            // then
            assertThrows(ProjectNotAllowException.class, () -> projectMemberService.deleteMember(projectId, userId, memberId));
        }
    }
}
