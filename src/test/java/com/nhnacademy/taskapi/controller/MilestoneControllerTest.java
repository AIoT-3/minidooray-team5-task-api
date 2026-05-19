package com.nhnacademy.taskapi.controller;

import com.nhnacademy.taskapi.dto.milestone.MilestoneCreateRequest;
import com.nhnacademy.taskapi.dto.milestone.MilestoneResponse;
import com.nhnacademy.taskapi.dto.milestone.MilestoneUpdateRequest;
import com.nhnacademy.taskapi.entity.MilestoneStatus;
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

@WebMvcTest(MilestoneController.class)
public class MilestoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MilestoneController controller;

    private final String BASE_URL="/api/tasks/projects/{projectId}/milestones";
    private final String USER_ID_HEADER="X-User-Id";

    private final Long projectId=1L;
    private final String userId="testUser";

    @Test
    @DisplayName("마일스톤 리스트 조회")
    void getMilestones() throws Exception {
        // given
        MilestoneResponse resp1=new MilestoneResponse(
                1L,
                "Milestone 1",
                MilestoneStatus.IN_PROGRESS
        );
        MilestoneResponse resp2=new MilestoneResponse(
                2L,
                "Milestone 2",
                MilestoneStatus.OPEN
        );
        given(controller.getMilestonesByProject(projectId, userId))
                .willReturn(ResponseEntity.ok(List.of(resp1, resp2)));

        // when & then
        mockMvc.perform(get(BASE_URL, projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].milestoneId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Milestone 1"))
                .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$[1].milestoneId").value(2L))
                .andExpect(jsonPath("$[1].name").value("Milestone 2"))
                .andExpect(jsonPath("$[1].status").value("OPEN"));
    }

    @Test
    @DisplayName("마일스톤 생성")
    void createMilestone() throws Exception {
        // given
        MilestoneResponse resp=new MilestoneResponse(
                1L,
                "Milestone 1",
                MilestoneStatus.IN_PROGRESS
        );
        given(controller.createMilestone(projectId, userId, new MilestoneCreateRequest("Milestone 1")))
                .willReturn(ResponseEntity.ok(resp));

        // when & then
        mockMvc.perform(post(BASE_URL, projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId)
                        .content(
                                """
                                {
                                    "name": "Milestone 1"
                                }
                                """
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneId").value(1L))
                .andExpect(jsonPath("$.name").value("Milestone 1"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("마일스톤 단건 조회")
    void getMilestone() throws Exception {
        // given
        Long milestoneId=1L;
        MilestoneResponse resp=new MilestoneResponse(
                milestoneId,
                "Milestone 1",
                MilestoneStatus.IN_PROGRESS
        );
        given(controller.getMilestone(projectId, milestoneId, userId))
                .willReturn(ResponseEntity.ok(resp));

        // when & then
        mockMvc.perform(get(BASE_URL + "/{milestoneId}", projectId, milestoneId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneId").value(milestoneId))
                .andExpect(jsonPath("$.name").value("Milestone 1"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("마일스톤 수정")
    void updateMilestone() throws Exception {
        // given
        Long milestoneId=1L;
        MilestoneResponse resp=new MilestoneResponse(
                milestoneId,
                "Milestone 1 Updated",
                MilestoneStatus.IN_PROGRESS
        );
        given(controller.updateMilestone(projectId, milestoneId, userId, new MilestoneUpdateRequest("Milestone 1 Updated", MilestoneStatus.IN_PROGRESS)))
                .willReturn(ResponseEntity.ok(resp));

        // when & then
        mockMvc.perform(post(BASE_URL + "/{milestoneId}", projectId, milestoneId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId)
                        .content(
                                """
                                {
                                    "name": "Milestone 1 Updated",
                                    "status": "IN_PROGRESS"
                                }
                                """
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneId").value(milestoneId))
                .andExpect(jsonPath("$.name").value("Milestone 1 Updated"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("마일스톤 삭제")
    void deleteMilestone() throws Exception {
        // given
        Long milestoneId=1L;
        given(controller.deleteMilestone(projectId, milestoneId, userId))
                .willReturn(ResponseEntity.noContent().build());

        // when & then
        mockMvc.perform(delete(BASE_URL + "/{milestoneId}", projectId, milestoneId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isNoContent());
    }
}
