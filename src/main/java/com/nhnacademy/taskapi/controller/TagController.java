package com.nhnacademy.taskapi.controller;

import com.nhnacademy.taskapi.dto.tag.TagCreateRequest;
import com.nhnacademy.taskapi.dto.tag.TagResponse;
import com.nhnacademy.taskapi.dto.tag.TagUpdateRequest;
import com.nhnacademy.taskapi.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tags")
public class TagController {

    private final TagService tagService;

    // 프로젝트 내 모든 태그 조회
    @GetMapping
    public ResponseEntity<List<TagResponse>> getTagsByProject(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("X-User-Id") String userId
    ) {
        List<TagResponse> resp=tagService.getTagsByProjectId(projectId, userId);

        return ResponseEntity.ok(resp);
    }

    // 태그 생성
    @PostMapping
    public ResponseEntity<TagResponse> createTag(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody TagCreateRequest req
    ) {
        TagResponse resp=tagService.createTag(projectId, userId, req);

        return ResponseEntity.ok(resp);
    }

    // 태그 업데이트
    @PostMapping("/{tagId}")
    public ResponseEntity<TagResponse> updateTag(
            @PathVariable("projectId") Long projectId,
            @PathVariable("tagId") Long tagId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody TagUpdateRequest req
    ) {
        TagResponse resp=tagService.updateTag(projectId, userId, tagId, req);

        return ResponseEntity.ok(resp);
    }

    // 태그 삭제
    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(
            @PathVariable("projectId") Long projectId,
            @PathVariable("tagId") Long tagId,
            @RequestHeader("X-User-Id") String userId
    ) {
        tagService.deleteTag(projectId, userId, tagId);

        return ResponseEntity.noContent().build();
    }
}
