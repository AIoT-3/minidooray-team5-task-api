package com.nhnacademy.taskapi.controller;

import com.nhnacademy.taskapi.dto.comment.CommentCreateRequest;
import com.nhnacademy.taskapi.dto.comment.CommentResponse;
import com.nhnacademy.taskapi.dto.comment.CommentUpdateRequest;
import com.nhnacademy.taskapi.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService service;

    private final String BASE_URL="/api/tasks/projects/{projectId}/tasks/{taskId}/comments";
    private final String USER_ID_HEADER="X-User-Id";

    private final Long projectId=1L;
    private final Long taskId=1L;
    private final String userId="testUser";

    @Test
    @DisplayName("댓글 리스트 조회")
    void getComments() throws Exception {
        //given
        CommentResponse resp1=new CommentResponse(
                1L,
                taskId,
                userId,
                "content 1"
        );

        CommentResponse resp2=new CommentResponse(
                2L,
                taskId,
                userId,
                "content 2"
        );

        given(service.getCommentsByTask(projectId, userId, taskId))
                .willReturn(List.of(resp1, resp2));

        // when & then
        mockMvc.perform(get(BASE_URL, projectId, taskId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(USER_ID_HEADER, userId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].commentId").value(resp1.commentId()))
                .andExpect(jsonPath("$[0].content").value(resp1.content()))
                .andExpect(jsonPath("$[1].commentId").value(resp2.commentId()))
                .andExpect(jsonPath("$[1].content").value(resp2.content()));;
    }

    @Test
    @DisplayName("댓글 생성")
    void createComment() throws Exception {
        // given
        CommentResponse resp=new CommentResponse(
                1L,
                taskId,
                userId,
                "content 1"
        );

        given(service.createComment(projectId, userId, taskId, new CommentCreateRequest("content 1")))
                .willReturn(resp);

        // when & then
        mockMvc.perform(post(BASE_URL, projectId, taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(USER_ID_HEADER, userId)
                        .content("""
                                {
                                    "content": "content 1"
                                }
                                """
                        )
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(resp.commentId()))
                .andExpect(jsonPath("$.content").value(resp.content()));
    }

    @Test
    @DisplayName("댓글 단건 조회")
    void getComment() throws Exception {
        // given
        Long commentId=1L;
        CommentResponse resp=new CommentResponse(
                commentId,
                taskId,
                userId,
                "content 1"
        );

        given(service.getComment(projectId, userId, taskId, commentId))
                .willReturn(resp);

        // when & then
        mockMvc.perform(get(BASE_URL+"/{commentId}", projectId, taskId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(resp.commentId()))
                .andExpect(jsonPath("$.content").value(resp.content()));
    }

    @Test
    @DisplayName("댓글 수정")
    void updateComment() throws Exception {
        // given
        Long commentId=1L;
        CommentResponse resp=new CommentResponse(
                commentId,
                taskId,
                userId,
                "updated content"
        );

        given(service.updateComment(projectId, userId, taskId, commentId, new CommentUpdateRequest("updated content")))
                .willReturn(resp);

        // when & then
        mockMvc.perform(post(BASE_URL+"/{commentId}", projectId, taskId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId)
                        .content("""
                                {
                                    "content": "updated content"
                                }
                                """
                        )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(resp.commentId()))
                .andExpect(jsonPath("$.content").value(resp.content()));
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteComment() throws Exception {
        // given
        Long commentId=1L;


        // when & then
        mockMvc.perform(delete(BASE_URL+"/{commentId}", projectId, taskId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId)
                )
                .andExpect(status().isNoContent());
    }
}
