package com.nhnacademy.taskapi.controller;

import com.nhnacademy.taskapi.dto.tag.TagCreateRequest;
import com.nhnacademy.taskapi.dto.tag.TagResponse;
import com.nhnacademy.taskapi.dto.tag.TagUpdateRequest;
import com.nhnacademy.taskapi.repository.TagRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tags")
public class TagController {

    private final TagRepository tagRepository;

    @GetMapping
    public ResponseEntity<TagResponse> getTagsByProject(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("X-User-Id") String userId
    ) {

        return null;
    }

    @PostMapping
    public ResponseEntity<TagResponse> createTag(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody TagCreateRequest req
    ) {

        return null;
    }

    @PostMapping("/{tagId}")
    public ResponseEntity<TagResponse> updateTag(
            @PathVariable("projectId") Long projectId,
            @PathVariable("tagId") Long tagId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody TagUpdateRequest req
    ) {

        return null;
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(
            @PathVariable("projectId") Long projectId,
            @PathVariable("tagId") Long tagId,
            @RequestHeader("X-User-Id") String userId
    ) {

        return null;
    }
}
