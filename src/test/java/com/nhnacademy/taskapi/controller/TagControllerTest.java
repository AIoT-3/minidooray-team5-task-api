package com.nhnacademy.taskapi.controller;

import com.nhnacademy.taskapi.dto.tag.TagCreateRequest;
import com.nhnacademy.taskapi.dto.tag.TagResponse;
import com.nhnacademy.taskapi.dto.tag.TagUpdateRequest;
import com.nhnacademy.taskapi.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TagService service;

    private final String BASE_URL="/api/tasks/projects/{projectId}/tags";
    private final String USER_ID_HEADER="X-User-Id";

    private final Long projectId=1L;
    private final Long tagId=1L;
    private final String userId="testUser";

    @Test
    @DisplayName("태그 리스트 조회")
    void getTagsByProject() throws Exception {
        // given
        TagResponse resp1=new TagResponse(
                tagId,
                "Tag 1"
        );
        TagResponse resp2=new TagResponse(
                2L,
                "Tag 2"
        );

        given(service.getTagsByProjectId(projectId, userId))
                .willReturn(List.of(resp1, resp2));

        // when & then
        mockMvc.perform(get(BASE_URL, projectId)
                        .contentType("application/json")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tagId").value(tagId))
                .andExpect(jsonPath("$[0].name").value("Tag 1"))
                .andExpect(jsonPath("$[1].tagId").value(2L))
                .andExpect(jsonPath("$[1].name").value("Tag 2"));
    }

    @Test
    @DisplayName("태그 생성")
    void createTag() throws Exception {
        // given
        TagResponse resp=new TagResponse(
                tagId,
                "New Tag"
        );

        given(service.createTag(projectId, userId, new TagCreateRequest("New Tag")))
                .willReturn(resp);

        // when & then
        mockMvc.perform(post(BASE_URL, projectId)
                        .contentType("application/json")
                        .header(USER_ID_HEADER, userId)
                        .content("{\"name\": \"New Tag\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagId").value(tagId))
                .andExpect(jsonPath("$.name").value("New Tag"));
    }

    @Test
    @DisplayName("태그 업데이트")
    void updateTag() throws Exception {
        // given
        TagResponse resp=new TagResponse(
                tagId,
                "Updated Tag"
        );

        given(service.updateTag(projectId, userId, tagId, new TagUpdateRequest("Updated Tag")))
                .willReturn(resp);

        // when & then
        mockMvc.perform(post(BASE_URL + "/{tagId}", projectId, tagId)
                        .contentType("application/json")
                        .header(USER_ID_HEADER, userId)
                        .content("{\"name\": \"Updated Tag\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagId").value(tagId))
                .andExpect(jsonPath("$.name").value("Updated Tag"));
    }

    @Test
    @DisplayName("태그 삭제")
    void deleteTag() throws Exception {
        // given

        // when & then
        mockMvc.perform(delete(BASE_URL + "/{tagId}", projectId, tagId)
                        .contentType("application/json")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isNoContent());
    }
}
