package com.nhnacademy.taskapi.service;

import com.nhnacademy.taskapi.dto.tag.TagCreateRequest;
import com.nhnacademy.taskapi.dto.tag.TagResponse;
import com.nhnacademy.taskapi.dto.tag.TagUpdateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TagService {
    List<TagResponse> getTagsByProjectId(Long projectId, String userId);

    TagResponse createTag(Long projectId, String userId, TagCreateRequest req);

    TagResponse updateTag(Long projectId, String userId, Long tagId, TagUpdateRequest req);

    void deleteTag(Long projectId, String userId, Long tagId);
}
