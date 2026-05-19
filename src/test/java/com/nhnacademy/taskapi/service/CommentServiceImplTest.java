package com.nhnacademy.taskapi.service;

import com.nhnacademy.taskapi.dto.comment.CommentCreateRequest;
import com.nhnacademy.taskapi.dto.comment.CommentResponse;
import com.nhnacademy.taskapi.dto.comment.CommentUpdateRequest;
import com.nhnacademy.taskapi.entity.Comment;
import com.nhnacademy.taskapi.entity.Task;
import com.nhnacademy.taskapi.exception.allow.ex.CommentNotAllowException;
import com.nhnacademy.taskapi.exception.notfound.ex.CommentNotFoundException;
import com.nhnacademy.taskapi.exception.notfound.ex.TaskNotFoundException;
import com.nhnacademy.taskapi.repository.CommentRepository;
import com.nhnacademy.taskapi.repository.ProjectMemberRepository;
import com.nhnacademy.taskapi.repository.TaskRepository;
import com.nhnacademy.taskapi.service.impl.CommentServiceImpl;
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
public class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    private final String userId="user1";
    private final Long projectId=1L;
    private final Long taskId=1L;

    @BeforeEach
    void setup() {
        when(projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)).thenReturn(true);
    }

    @Nested
    @DisplayName("댓글 리스트 조회")
    class getCommentsByTask {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Task task=Task.builder()
                    .title("task1")
                    .content("content1")
                    .userId(userId)
                    .build();
            ReflectionTestUtils.setField(task, "id", taskId);

            Comment comment1=Comment.builder()
                    .userId(userId)
                    .content("comment1")
                    .task(task)
                    .build();
            ReflectionTestUtils.setField(comment1, "id", 1L);

            Comment comment2=Comment.builder()
                    .userId(userId)
                    .content("comment2")
                    .task(task)
                    .build();
            ReflectionTestUtils.setField(comment2, "id", 2L);

            // when
            when(commentRepository.findAllByTask_Id(taskId)).thenReturn(List.of(comment1, comment2));
            List<CommentResponse> resp=commentService.getCommentsByTask(projectId, userId, taskId);

            // then
            assertAll(
                    () -> assertEquals(2, resp.size()),
                    () -> assertEquals(1L, resp.get(0).commentId()),
                    () -> assertEquals(taskId, resp.get(0).taskId()),
                    () -> assertEquals(userId, resp.get(0).writerUserId()),
                    () -> assertEquals("comment1", resp.get(0).content()),
                    () -> assertEquals(2L, resp.get(1).commentId()),
                    () -> assertEquals(taskId, resp.get(1).taskId()),
                    () -> assertEquals(userId, resp.get(1).writerUserId()),
                    () -> assertEquals("comment2", resp.get(1).content())
            );
        }
    }

    @Nested
    @DisplayName("댓글 생성")
    class createComment {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            String content="comment1";

            Task task=Task.builder()
                    .title("task1")
                    .content("content1")
                    .userId(userId)
                    .build();
            ReflectionTestUtils.setField(task, "id", taskId);

            Comment comment=Comment.builder()
                    .userId(userId)
                    .content(content)
                    .task(task)
                    .build();
            ReflectionTestUtils.setField(comment, "id", 1L);

            // when
            when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task));
            when(commentRepository.save(any(Comment.class))).thenReturn(comment);
            CommentResponse resp=commentService.createComment(projectId, userId, taskId, new CommentCreateRequest(content));

            // then
            assertAll(
                    () -> assertEquals(1L, resp.commentId()),
                    () -> assertEquals(taskId, resp.taskId()),
                    () -> assertEquals(userId, resp.writerUserId()),
                    () -> assertEquals(content, resp.content())
            );
        }

        @Test
        @DisplayName("실패 - 테스크 없음")
        void fail_taskNotFound() {
            // given
            String content="comment1";
            CommentCreateRequest req=new CommentCreateRequest(content);

            // when
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

            // then
            assertThrows(
                    TaskNotFoundException.class,
                    () -> commentService.createComment(projectId, userId, taskId, req)
            );
        }
    }

    @Nested
    @DisplayName("댓글 단건 조회")
    class getComment {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long commentId = 1L;
            String content = "comment1";

            Task task = Task.builder()
                    .title("task1")
                    .content("content1")
                    .userId(userId)
                    .build();
            ReflectionTestUtils.setField(task, "id", taskId);

            Comment comment = Comment.builder()
                    .userId(userId)
                    .content(content)
                    .task(task)
                    .build();
            ReflectionTestUtils.setField(comment, "id", commentId);

            // when
            when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(comment));
            CommentResponse resp = commentService.getComment(projectId, userId, taskId, commentId);

            // then
            assertAll(
                    () -> assertEquals(commentId, resp.commentId()),
                    () -> assertEquals(taskId, resp.taskId()),
                    () -> assertEquals(userId, resp.writerUserId()),
                    () -> assertEquals(content, resp.content())
            );
        }

        @Test
        @DisplayName("실패 - 댓글 없음")
        void fail_commentNotFound() {
            // given
            Long commentId = 1L;

            // when
            when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

            // then
            assertThrows(
                    CommentNotFoundException.class,
                    () -> commentService.getComment(projectId, userId, taskId, commentId)
            );
        }

        @Test
        @DisplayName("실패 - 댓글이 해당 테스크의 댓글이 아님")
        void fail_commentNotAllow() {
            // given
            Long commentId = 1L;

            Task task = Task.builder()
                    .title("task1")
                    .content("content1")
                    .userId(userId)
                    .build();
            ReflectionTestUtils.setField(task, "id", taskId);

            Comment comment = Comment.builder()
                    .userId(userId)
                    .content("comment1")
                    .task(task)
                    .build();
            ReflectionTestUtils.setField(comment, "id", commentId);

            // when
            when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(comment));

            // then
            assertThrows(
                    CommentNotAllowException.class,
                    () -> commentService.getComment(projectId, userId, taskId+1, commentId)
            );
        }
    }

    @Nested
    @DisplayName("댓글 수정")
    class updateComment {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long commentId = 1L;
            String newContent = "updatedComment";

            Task task = Task.builder()
                    .title("task1")
                    .content("content1")
                    .userId(userId)
                    .build();
            ReflectionTestUtils.setField(task, "id", taskId);

            Comment comment = Comment.builder()
                    .userId(userId)
                    .content("comment1")
                    .task(task)
                    .build();
            ReflectionTestUtils.setField(comment, "id", commentId);

            // when
            when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(comment));
            CommentResponse resp = commentService.updateComment(projectId, userId, taskId, commentId, new CommentUpdateRequest(newContent));

            // then
            assertAll(
                    () -> assertEquals(commentId, resp.commentId()),
                    () -> assertEquals(taskId, resp.taskId()),
                    () -> assertEquals(userId, resp.writerUserId()),
                    () -> assertEquals(newContent, resp.content())
            );
        }

        @Test
        @DisplayName("실패 - 댓글 없음")
        void fail_commentNotFound() {
            // given
            Long commentId = 1L;
            String newContent = "updatedComment";
            CommentUpdateRequest req=new CommentUpdateRequest(newContent);

            // when
            when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

            // then
            assertThrows(
                    CommentNotFoundException.class,
                    () -> commentService.updateComment(projectId, userId, taskId, commentId, req)
            );
        }

        @Test
        @DisplayName("실패 - 해당 테스크의 댓글이 아님")
        void fail_commentNotAllow() {
            // given
            Long commentId = 1L;
            String newContent = "updatedComment";

            Task task = Task.builder()
                    .title("task1")
                    .content("content1")
                    .userId(userId)
                    .build();
            ReflectionTestUtils.setField(task, "id", 2L);

            Comment comment = Comment.builder()
                    .userId(userId)
                    .content("comment1")
                    .task(task)
                    .build();
            ReflectionTestUtils.setField(comment, "id", commentId);
            CommentUpdateRequest req=new CommentUpdateRequest(newContent);

            // when
            when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(comment));

            // then
            assertThrows(
                    CommentNotAllowException.class,
                    () -> commentService.updateComment(projectId, userId, taskId, commentId, req)
            );
        }

        @Test
        @DisplayName("실패 - 댓글 작성자가 아님")
        void fail_commentNotAllow2() {
            // given
            Long commentId = 1L;
            String newContent = "updatedComment";

            Task task = Task.builder()
                    .title("task1")
                    .content("content1")
                    .userId(userId)
                    .build();
            ReflectionTestUtils.setField(task, "id", taskId);

            Comment comment = Comment.builder()
                    .userId("otherUser")
                    .content("comment1")
                    .task(task)
                    .build();
            ReflectionTestUtils.setField(comment, "id", commentId);
            CommentUpdateRequest req= new CommentUpdateRequest(newContent);

            // when
            when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(comment));

            // then
            assertThrows(
                    CommentNotAllowException.class,
                    () -> commentService.updateComment(projectId, userId, taskId, commentId, req)
            );
        }
    }

    @Nested
    @DisplayName("댓글 삭제")
    class deleteComment {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long commentId = 1L;

            Task task = Task.builder()
                    .title("task1")
                    .content("content1")
                    .userId(userId)
                    .build();
            ReflectionTestUtils.setField(task, "id", taskId);

            Comment comment = Comment.builder()
                    .userId(userId)
                    .content("comment1")
                    .task(task)
                    .build();
            ReflectionTestUtils.setField(comment, "id", commentId);

            // when
            when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(comment));

            // then
            assertDoesNotThrow(
                    () -> commentService.deleteComment(projectId, userId, taskId, commentId)
            );
        }

        @Test
        @DisplayName("실패 - 댓글 없음")
        void fail_commentNotFound() {
            // given
            Long commentId = 1L;

            // when
            when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

            // then
            assertThrows(
                    CommentNotFoundException.class,
                    () -> commentService.deleteComment(projectId, userId, taskId, commentId)
            );
        }

        @Test
        @DisplayName("실패 - 해당 테스크의 댓글이 아님")
        void fail_commentNotAllow() {
            // given
            Long commentId = 1L;

            Task task = Task.builder()
                    .title("task1")
                    .content("content1")
                    .userId(userId)
                    .build();
            ReflectionTestUtils.setField(task, "id", 2L);

            Comment comment = Comment.builder()
                    .userId(userId)
                    .content("comment1")
                    .task(task)
                    .build();
            ReflectionTestUtils.setField(comment, "id", commentId);

            // when
            when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(comment));

            // then
            assertThrows(
                    CommentNotAllowException.class,
                    () -> commentService.deleteComment(projectId, userId, taskId, commentId)
            );
        }

        @Test
        @DisplayName("실패 - 댓글 작성자가 아님")
        void fail_commentNotAllow2() {
            // given
            Long commentId = 1L;

            Task task = Task.builder()
                    .title("task1")
                    .content("content1")
                    .userId(userId)
                    .build();
            ReflectionTestUtils.setField(task, "id", taskId);

            Comment comment = Comment.builder()
                    .userId("otherUser")
                    .content("comment1")
                    .task(task)
                    .build();
            ReflectionTestUtils.setField(comment, "id", commentId);

            // when
            when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(comment));

            // then
            assertThrows(
                    CommentNotAllowException.class,
                    () -> commentService.deleteComment(projectId, userId, taskId, commentId)
            );
        }
    }
}
