package com.nhnacademy.taskapi.controller;

import com.nhnacademy.taskapi.dto.task.TaskCreateRequest;
import com.nhnacademy.taskapi.dto.task.TaskDetailResponse;
import com.nhnacademy.taskapi.dto.task.TaskSummaryResponse;
import com.nhnacademy.taskapi.dto.task.TaskUpdateRequest;
import com.nhnacademy.taskapi.service.TaskService;
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

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService service;

    private final String BASE_URL="/api/tasks/projects/{projectId}/tasks";
    private final String USER_ID_HEADER="X-User-Id";

    private final Long projectId=1L;
    private final String userId="testUser";

    @Test
    @DisplayName("테스크 리스트 조회")
    void getTasks() throws Exception {
        // given
        TaskSummaryResponse resp1=new TaskSummaryResponse(
                1L,
                "Task 1",
                null,
                List.of(),
                0
        );
        TaskSummaryResponse resp2=new TaskSummaryResponse(
                2L,
                "Task 2",
                null,
                List.of(),
                2
        );
        given(service.getTasks(projectId, userId))
                .willReturn(List.of(resp1, resp2));

        // when & then
        mockMvc.perform(get(BASE_URL, projectId)
                        .contentType("application/json")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taskId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].milestoneName").isEmpty())
                .andExpect(jsonPath("$[0].tags").isEmpty())
                .andExpect(jsonPath("$[0].commentCount").value(0))
                .andExpect(jsonPath("$[1].taskId").value(2L))
                .andExpect(jsonPath("$[1].title").value("Task 2"))
                .andExpect(jsonPath("$[1].milestoneName").isEmpty())
                .andExpect(jsonPath("$[1].tags").isEmpty())
                .andExpect(jsonPath("$[1].commentCount").value(2));
    }

    @Test
    @DisplayName("테스크 생성")
    void createTask() throws Exception {
        // given
        TaskDetailResponse resp=new TaskDetailResponse(
                1L,
                projectId,
                "New Task",
                "Task description",
                null,
                List.of()
        );
        given(service.createTask(projectId, userId, new TaskCreateRequest("New Task", "Task description", projectId)))
                .willReturn(resp);

        // when & then
        mockMvc.perform(post(BASE_URL, projectId)
                        .contentType("application/json")
                        .header(USER_ID_HEADER, userId)
                        .content("{\"title\": \"New Task\", \"content\": \"Task description\", \"projectId\": "+projectId+"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(1L))
                .andExpect(jsonPath("$.projectId").value(projectId))
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.content").value("Task description"))
                .andExpect(jsonPath("$.milestone").isEmpty())
                .andExpect(jsonPath("$.tags").isEmpty());
    }

    @Test
    @DisplayName("테스크 단건 조회")
    void getTask() throws Exception {
        // given
        Long taskId=1L;
        TaskDetailResponse resp=new TaskDetailResponse(
                taskId,
                projectId,
                "Existing Task",
                "Existing description",
                null,
                List.of()
        );
        given(service.getTask(projectId, taskId, userId))
                .willReturn(resp);

        // when & then
        mockMvc.perform(get(BASE_URL+"/{taskId}", projectId, taskId)
                        .contentType("application/json")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(taskId))
                .andExpect(jsonPath("$.projectId").value(projectId))
                .andExpect(jsonPath("$.title").value("Existing Task"))
                .andExpect(jsonPath("$.content").value("Existing description"))
                .andExpect(jsonPath("$.milestone").isEmpty())
                .andExpect(jsonPath("$.tags").isEmpty());
    }

    @Test
    @DisplayName("테스트 수정")
    void updateTask() throws Exception {
        // given
        Long taskId=1L;
        TaskDetailResponse resp=new TaskDetailResponse(
                taskId,
                projectId,
                "Updated Task",
                "Updated description",
                null,
                List.of()
        );
        given(service.updateTask(projectId, userId, taskId, new TaskUpdateRequest("Updated Task", "Updated description", null, List.of())))
                .willReturn(resp);

        // when & then
        mockMvc.perform(post(BASE_URL+"/{taskId}", projectId, taskId)
                        .contentType("application/json")
                        .header(USER_ID_HEADER, userId)
                        .content("{\"title\": \"Updated Task\", \"content\": \"Updated description\", \"milestoneId\": "+null+", \"tagIds\": []}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(taskId))
                .andExpect(jsonPath("$.projectId").value(projectId))
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.content").value("Updated description"))
                .andExpect(jsonPath("$.milestone").isEmpty())
                .andExpect(jsonPath("$.tags").isEmpty());
    }

    @Test
    @DisplayName("테스크 삭제")
    void deleteTask() throws Exception {
        // given
        Long taskId=1L;

        // when & then
        mockMvc.perform(delete(BASE_URL+"/{taskId}", projectId, taskId)
                        .contentType("application/json")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isNoContent());
    }
}
