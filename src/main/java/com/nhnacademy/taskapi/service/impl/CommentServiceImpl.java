package com.nhnacademy.taskapi.service.impl;

import com.nhnacademy.taskapi.dto.comment.CommentCreateRequest;
import com.nhnacademy.taskapi.dto.comment.CommentResponse;
import com.nhnacademy.taskapi.dto.comment.CommentUpdateRequest;
import com.nhnacademy.taskapi.entity.Comment;
import com.nhnacademy.taskapi.entity.Task;
import com.nhnacademy.taskapi.exception.ResourceNotAllowException;
import com.nhnacademy.taskapi.exception.ResourceNotFoundException;
import com.nhnacademy.taskapi.repository.CommentRepository;
import com.nhnacademy.taskapi.repository.ProjectMemberRepository;
import com.nhnacademy.taskapi.repository.TaskRepository;
import com.nhnacademy.taskapi.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;

    // 프로젝트 내 테스트의 모든 댓글 조회
    @Override
    public List<CommentResponse> getCommentsByTask(Long projectId, String userId, Long taskId) {
        checkUser(projectId, userId);

        // 프로젝트 내 테스트의 모든 댓글 조회
        List<Comment> comments=commentRepository.findAllByTask_Id(taskId);

        // 응답 반환 Comment -> CommentResponse
        return comments.stream().map(
                c -> CommentResponse.builder()
                        .commentId(c.getId())
                        .taskId(c.getTask().getId())
                        .writerUserId(c.getUserId())
                        .content(c.getContent())
                        .build()
        ).toList();
    }

    // 댓글 생성
    @Override
    @Transactional
    public CommentResponse createComment(Long projectId, String userId, Long taskId, CommentCreateRequest req) {
        checkUser(projectId, userId);

        // 댓글 생성
        Comment comment=Comment.builder()
                .userId(userId)
                .content(req.content())
                .build();

        // 테스크 조회
        Task task=taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException(taskId+"의 테스크가 없습니다."));

        // 댓글에 테스크 설정
        comment.setTask(task);

        // 댓글 저장
        Comment savedComment=commentRepository.save(comment);

        // 응답 반환 Comment -> CommentResponse
        return CommentResponse.builder()
                .commentId(savedComment.getId())
                .taskId(savedComment.getTask().getId())
                .writerUserId(savedComment.getUserId())
                .content(savedComment.getContent())
                .build();
    }

    // 댓글 단건 조회
    @Override
    public CommentResponse getComment(Long projectId, String userId, Long taskId, Long commentId) {
        checkUser(projectId, userId);

        // 댓글 조회
        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(commentId+"의 댓글이 없습니다."));

        // 댓글이 해당 테스크의 댓글인지 확인
        if(!Objects.equals(comment.getTask().getId(), taskId)) {
            throw new ResourceNotAllowException("테스크 "+taskId+"의 댓글이 아닙니다.");
        }

        // 응답 반환 Comment -> CommentResponse
        return CommentResponse.builder()
                .commentId(comment.getId())
                .taskId(comment.getTask().getId())
                .writerUserId(comment.getUserId())
                .content(comment.getContent())
                .build();
    }

    // 댓글 수정
    @Override
    @Transactional
    public CommentResponse updateComment(Long projectId, String userId, Long taskId,
                                         Long commentId, CommentUpdateRequest req) {
        checkUser(projectId, userId);

        // 댓글 조회
        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(commentId+"의 댓글이 없습니다."));

        // 댓글이 해당 테스크의 댓글인지 확인
        if(!Objects.equals(comment.getTask().getId(), taskId)) {
            throw new ResourceNotAllowException("테스크 "+taskId+"의 댓글이 아닙니다.");
        }

        // 댓글 작성자 본인인지 확인
        if(!Objects.equals(comment.getUserId(), userId)) {
            throw new ResourceNotAllowException("댓글 작성자만 수정할 수 있습니다.");
        }

        // 댓글 수정
        comment.setContent(req.content());

        // 응답 반환 Comment -> CommentResponse
        return CommentResponse.builder()
                .commentId(comment.getId())
                .taskId(comment.getTask().getId())
                .writerUserId(comment.getUserId())
                .content(comment.getContent())
                .build();
    }

    // 댓글 삭제
    @Override
    @Transactional
    public void deleteComment(Long projectId, String userId, Long taskId, Long commentId) {
        checkUser(projectId, userId);

        // 댓글 조회
        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(commentId+"의 댓글이 없습니다."));

        // 댓글이 해당 테스크의 댓글인지 확인
        if(!Objects.equals(comment.getTask().getId(), taskId)) {
            throw new ResourceNotAllowException("테스크 "+taskId+"의 댓글이 아닙니다.");
        }

        // 댓글 작성자 본인인지 확인
        if(!Objects.equals(comment.getUserId(), userId)) {
            throw new ResourceNotAllowException("댓글 작성자만 삭제할 수 있습니다.");
        }

        // 댓글 삭제
        commentRepository.delete(comment);
    }

    // 유저가 프로젝트 멤버인지 확인
    private void checkUser(Long projectId, String userId) {
        if(!projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)) {
            throw new ResourceNotFoundException("프로젝트 멤버가 아닙니다.");
        }
    }
}
