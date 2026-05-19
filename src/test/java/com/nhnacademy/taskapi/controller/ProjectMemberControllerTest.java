package com.nhnacademy.taskapi.controller;

import com.nhnacademy.taskapi.dto.project_member.ProjectMemberAddRequest;
import com.nhnacademy.taskapi.dto.project_member.ProjectMemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectMemberController.class)
public class ProjectMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectMemberController controller;

    private final String BASE_URL="/api/tasks/projects/{projectId}/members";
    private final String USER_ID_HEADER="X-User-Id";

    private final Long projectId=1L;
    private final String userId="testUser";

    @Test
    @DisplayName("프로젝트 멤버 전체 조회")
    void getProjectMembers() throws Exception {
        // given
        ProjectMemberResponse resp1=new ProjectMemberResponse(
                userId,
                true
        );
        ProjectMemberResponse resp2=new ProjectMemberResponse(
                "testUser2",
                false
        );
        given(controller.getProjectMembers(projectId, userId))
                .willReturn(ResponseEntity.ok(List.of(resp1, resp2)));

        // when & then
        mockMvc.perform(get(BASE_URL, projectId)
                        .contentType("application/json")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].admin").value(true))
                .andExpect(jsonPath("$[1].userId").value("testUser2"))
                .andExpect(jsonPath("$[1].admin").value(false));
    }

    @Test
    @DisplayName("프로젝트 멤버 추가")
    void addProjectMember() throws Exception {
        // given
        String newMemberId="newUser";
        ProjectMemberResponse resp=new ProjectMemberResponse(
                newMemberId,
                false
        );
        given(controller.addProjectMember(projectId, userId, new ProjectMemberAddRequest(newMemberId)))
                .willReturn(ResponseEntity.ok(resp));

        // when & then
        mockMvc.perform(post(BASE_URL, projectId)
                        .contentType("application/json")
                        .header(USER_ID_HEADER, userId)
                        .content("{\"userId\":\""+newMemberId+"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(newMemberId))
                .andExpect(jsonPath("$.admin").value(false));
    }

    @Test
    @DisplayName("프로젝트 멤버 삭제")
    void removeProjectMember() throws Exception {
        // given
        String memberIdToRemove="removeUser";
        given(controller.removeProjectMember(projectId, memberIdToRemove, userId))
                .willReturn(ResponseEntity.noContent().build());

        // when & then
        mockMvc.perform(delete(BASE_URL+"/{userId}", projectId, memberIdToRemove)
                        .contentType("application/json")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isNoContent());
    }
}
