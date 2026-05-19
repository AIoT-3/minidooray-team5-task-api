package com.nhnacademy.taskapi.service;

import com.nhnacademy.taskapi.dto.tag.TagCreateRequest;
import com.nhnacademy.taskapi.dto.tag.TagResponse;
import com.nhnacademy.taskapi.dto.tag.TagUpdateRequest;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.Tag;
import com.nhnacademy.taskapi.exception.notfound.ex.ProjectNotFoundException;
import com.nhnacademy.taskapi.exception.notfound.ex.TagNotFoundException;
import com.nhnacademy.taskapi.repository.*;
import com.nhnacademy.taskapi.service.impl.TagServiceImpl;
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
public class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private TaskTagRepository taskTagRepository;

    private final String userId="user1";
    private final Long projectId=1L;
    private final Project project=Project.builder()
            .name("project1")
            .build();

    @BeforeEach
    void setup() {
        when(projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)).thenReturn(true);
    }

    @Nested
    @DisplayName("태그 생성")
    class createTag {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            TagCreateRequest req=new TagCreateRequest("tag1");
            Tag tag=Tag.builder()
                    .name(req.name())
                    .project(project)
                    .build();

            ReflectionTestUtils.setField(tag, "id", 1L);

            // when
            when(projectRepository.findById(projectId)).thenReturn(Optional.ofNullable(project));
            when(tagRepository.save(any(Tag.class))).thenReturn(tag);
            TagResponse resp=tagService.createTag(projectId, userId, req);

            // then
            assertAll(
                    () -> assertEquals(1L, resp.tagId()),
                    () -> assertEquals(req.name(), resp.name())
            );
        }

        @Test
        @DisplayName("실패 - 프로젝트 없음")
        void fail_projectNotFound() {
            // given
            TagCreateRequest req=new TagCreateRequest("tag1");

            // when
            when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

            // then
            assertThrows(ProjectNotFoundException.class, () -> tagService.createTag(projectId, userId, req));
        }
    }

    @Nested
    @DisplayName("태그 리스트 조회")
    class getTagsByProjectId {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Tag tag1 = Tag.builder()
                    .name("tag1")
                    .project(project)
                    .build();
            ReflectionTestUtils.setField(tag1, "id", 1L);

            Tag tag2 = Tag.builder()
                    .name("tag2")
                    .project(project)
                    .build();
            ReflectionTestUtils.setField(tag2, "id", 2L);

            // when
            when(tagRepository.findAllByProject_Id(projectId)).thenReturn(
                    List.of(tag1, tag2)
            );
            List<TagResponse> resp = tagService.getTagsByProjectId(projectId, userId);

            // then
            assertAll(
                    () -> assertEquals(2, resp.size()),
                    () -> assertEquals(1L, resp.get(0).tagId()),
                    () -> assertEquals("tag1", resp.get(0).name()),
                    () -> assertEquals(2L, resp.get(1).tagId()),
                    () -> assertEquals("tag2", resp.get(1).name())
            );
        }
    }

    @Nested
    @DisplayName("태그 업데이트")
    class updateTag {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long tagId = 1L;
            String newName = "newTagName";

            Tag tag = Tag.builder()
                    .name("tag1")
                    .project(project)
                    .build();
            ReflectionTestUtils.setField(tag, "id", tagId);

            // when
            when(tagRepository.findById(tagId)).thenReturn(Optional.ofNullable(tag));
            TagResponse resp = tagService.updateTag(projectId, userId, tagId, new TagUpdateRequest(newName));

            // then
            assertAll(
                    () -> assertEquals(tagId, resp.tagId()),
                    () -> assertEquals(newName, resp.name())
            );
        }

        @Test
        @DisplayName("실패 - 태그 없음")
        void fail_tagNotFound() {
            // given
            Long tagId = 1L;
            String newName = "newTagName";
            TagUpdateRequest req=new TagUpdateRequest(newName);

            // when
            when(tagRepository.findById(tagId)).thenReturn(Optional.empty());

            // then
            assertThrows(TagNotFoundException.class,
                    () -> tagService.updateTag(projectId, userId, tagId, req));
        }
    }

    @Nested
    @DisplayName("태그 삭제")
    class deleteTag {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long tagId = 1L;

            Tag tag = Tag.builder()
                    .name("tag1")
                    .project(project)
                    .build();
            ReflectionTestUtils.setField(tag, "id", tagId);

            // when
            when(tagRepository.findById(tagId)).thenReturn(Optional.ofNullable(tag));
            assertDoesNotThrow(() -> tagService.deleteTag(projectId, userId, tagId));
        }

        @Test
        @DisplayName("실패 - 태그 없음")
        void fail_tagNotFound() {
            // given
            Long tagId = 1L;

            // when
            when(tagRepository.findById(tagId)).thenReturn(Optional.empty());

            // then
            assertThrows(TagNotFoundException.class,
                    () -> tagService.deleteTag(projectId, userId, tagId));
        }
    }
}
