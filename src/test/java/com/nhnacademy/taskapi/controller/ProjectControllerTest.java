package com.nhnacademy.taskapi.controller;

import com.nhnacademy.taskapi.dto.project.ProjectCreateRequest;
import com.nhnacademy.taskapi.dto.project.ProjectResponse;
import com.nhnacademy.taskapi.dto.project.ProjectUpdateRequest;
import com.nhnacademy.taskapi.entity.ProjectStatus;
import com.nhnacademy.taskapi.service.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService service;

    private final String BASE_URL = "/api/tasks/projects";
    private final String USER_ID_HEADER = "X-User-Id";

    private final String userId = "testUser";
    private final Long projectId = 1L;

    @Test
    @DisplayName("프로젝트 리스트 조회")
    void getProjects() throws Exception {
        // given
        ProjectResponse resp1=new ProjectResponse(
                projectId,
                "Project 1",
                ProjectStatus.ACTIVE,
                true
        );
        ProjectResponse resp2=new ProjectResponse(
                2L,
                "Project 2",
                ProjectStatus.CLOSED,
                false
        );

        given(service.getProjects(userId))
                .willReturn(List.of(resp1, resp2));

        // when & then
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].projectId").value(projectId))
                .andExpect(jsonPath("$[0].name").value("Project 1"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[0].admin").value(true))
                .andExpect(jsonPath("$[1].projectId").value(2L))
                .andExpect(jsonPath("$[1].name").value("Project 2"))
                .andExpect(jsonPath("$[1].status").value("CLOSED"))
                .andExpect(jsonPath("$[1].admin").value(false));
    }

    @Test
    @DisplayName("프론젝트 생성")
    void createProject() throws Exception {
        // given
        ProjectResponse resp=new ProjectResponse(
                projectId,
                "Project 1",
                ProjectStatus.ACTIVE,
                true
        );

        given(service.createProject(new ProjectCreateRequest("Project 1"), userId))
                .willReturn(resp);

        // when & then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId)
                        .content(
                                """
                                {
                                    "name": "Project 1"
                                }
                                """
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(projectId))
                .andExpect(jsonPath("$.name").value("Project 1"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.admin").value(true));
    }

    @Test
    @DisplayName("프로젝트 단건 조회")
    void getProject() throws Exception {
        // given
        ProjectResponse resp=new ProjectResponse(
                projectId,
                "Project 1",
                ProjectStatus.ACTIVE,
                true
        );

        given(service.getProject(projectId, userId))
                .willReturn(resp);

        // when & then
        mockMvc.perform(get(BASE_URL+"/{projectId}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(projectId))
                .andExpect(jsonPath("$.name").value("Project 1"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.admin").value(true));
    }

    @Test
    @DisplayName("프로젝트 수정")
    void updateProject() throws Exception {
        // given
        ProjectResponse resp=new ProjectResponse(
                projectId,
                "Project 1 Updated",
                ProjectStatus.ACTIVE,
                true
        );

        given(service.updateProject(projectId, new ProjectUpdateRequest("Project 1 Updated", ProjectStatus.ACTIVE), userId))
                .willReturn(resp);

        // when & then
        mockMvc.perform(post(BASE_URL+"/{projectId}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId)
                        .content(
                                """
                                {
                                    "name": "Project 1 Updated",
                                    "status": "ACTIVE"
                                }
                                """
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(projectId))
                .andExpect(jsonPath("$.name").value("Project 1 Updated"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.admin").value(true));
    }

    @Test
    @DisplayName("프로젝트 삭제")
    void deleteProject() throws Exception {
        // given

        // when & then
        mockMvc.perform(delete(BASE_URL+"/{projectId}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isNoContent());
    }
}
