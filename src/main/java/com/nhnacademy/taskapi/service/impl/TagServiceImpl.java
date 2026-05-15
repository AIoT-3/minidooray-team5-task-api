package com.nhnacademy.taskapi.service.impl;

import com.nhnacademy.taskapi.dto.tag.TagCreateRequest;
import com.nhnacademy.taskapi.dto.tag.TagResponse;
import com.nhnacademy.taskapi.dto.tag.TagUpdateRequest;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.Tag;
import com.nhnacademy.taskapi.exception.ResourceNotFoundException;
import com.nhnacademy.taskapi.repository.ProjectMemberRepository;
import com.nhnacademy.taskapi.repository.ProjectRepository;
import com.nhnacademy.taskapi.repository.TagRepository;
import com.nhnacademy.taskapi.repository.TaskTagRepository;
import com.nhnacademy.taskapi.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TaskTagRepository taskTagRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    // 프로젝트 내 모든 태그 조회
    @Override
    public List<TagResponse> getTagsByProjectId(Long projectId, String userId) {
        checkUser(projectId, userId);

        // 프로젝트 내 모든 태그 조회
        List<Tag> tags=tagRepository.findAllByProject_Id(projectId);

        // 응답 반환 Tag -> TagResponse
        return tags.stream().map(
                t -> TagResponse.builder()
                        .tagId(t.getId())
                        .name(t.getName())
                        .build()
        ).toList();
    }

    // 태그 생성
    @Override
    @Transactional
    public TagResponse createTag(Long projectId, String userId, TagCreateRequest req) {
        checkUser(projectId, userId);

        // 태그 생성
        Tag tag=Tag.builder()
                .name(req.name())
                .build();

        // 프로젝트 조회
        Project project=projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));

        // 태그에 프로젝트 설정
        tag.setProject(project);

        // 태그 저장
        Tag savedTag=tagRepository.save(tag);

        // 응답 반환 Tag -> TagResponse
        return TagResponse.builder()
                .tagId(savedTag.getId())
                .name(savedTag.getName())
                .build();
    }

    // 태그 업데이트
    @Override
    @Transactional
    public TagResponse updateTag(Long projectId, String userId, Long tagId, TagUpdateRequest req) {
        checkUser(projectId, userId);

        // 태그 조회
        Tag tag=tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("태그를 찾을 수 없습니다."));

        // 태그 업데이트
        tag.setName(req.name());

        // 응답 반환 Tag -> TagResponse
        return TagResponse.builder()
                .tagId(tag.getId())
                .name(tag.getName())
                .build();
    }

    // 태그 삭제
    @Override
    @Transactional
    public void deleteTag(Long projectId, String userId, Long tagId) {
        checkUser(projectId, userId);

        // 태그 조회
        Tag tag=tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("태그를 찾을 수 없습니다."));

        // 태그와 연관된 TaskTag 삭제
        taskTagRepository.deleteByTag_Id(tagId);

        // 태그 삭제
        tagRepository.delete(tag);
    }

    // 유저가 프로젝트 멤버인지 확인
    private void checkUser(Long projectId, String userId) {
        if(!projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)) {
            throw new ResourceNotFoundException("프로젝트 멤버가 아닙니다.");
        }
    }
}
