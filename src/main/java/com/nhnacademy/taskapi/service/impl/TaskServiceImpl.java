package com.nhnacademy.taskapi.service.impl;

import com.nhnacademy.taskapi.dto.milestone.MilestoneResponse;
import com.nhnacademy.taskapi.dto.tag.TagResponse;
import com.nhnacademy.taskapi.dto.task.TaskCreateRequest;
import com.nhnacademy.taskapi.dto.task.TaskDetailResponse;
import com.nhnacademy.taskapi.dto.task.TaskSummaryResponse;
import com.nhnacademy.taskapi.dto.task.TaskUpdateRequest;
import com.nhnacademy.taskapi.entity.*;
import com.nhnacademy.taskapi.exception.ResourceNotFoundException;
import com.nhnacademy.taskapi.repository.*;
import com.nhnacademy.taskapi.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskTagRepository taskTagRepository;
    private final MilestoneRepository mileStoneRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

    // 프로젝트 내 모든 테스크 조회
    @Override
    public List<TaskSummaryResponse> getTasks(Long projectId, String userId) {
        checkUser(projectId, userId);

        // 프로젝트 내 모든 테스크 조회
        List<Task> tasks=taskRepository.findAllByProject_Id(projectId);

        // TaskSummaryResponse로 변환하여 반환
        return tasks.stream().map(task -> {
            // 마일스톤 이름
            String milestoneName=task.getMilestone()!=null?task.getMilestone().getName():null;

            // 태그 이름 리스트
            List<String> tags=task.getTaskTags().stream().map(
                    taskTag->taskTag.getTag().getName()
            ).toList();

            // 댓글 수
            long commentCount=task.getComments().size();

            // TaskSummaryResponse 생성
            return new TaskSummaryResponse(
                    task.getId(),
                    task.getTitle(),
                    milestoneName,
                    tags,
                    commentCount
            );
        }).toList();
    }

    // 테스크 생성
    @Override
    @Transactional
    public TaskDetailResponse createTask(Long projectId, String userId, TaskCreateRequest req) {
        checkUser(projectId, userId);

        // 테스크 생성
        Task task=Task.builder()
                .title(req.title())
                .content(req.content())
                .userId(userId)
                .build();

        // 프로젝트 조회
        Project project=projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(projectId+"번 프로젝트를 찾을 수 없습니다."));

        // 프로젝트 설정 -> 연관관계 설정
        task.setProject(project);

        // 테스크 저장
        Task savedTask=taskRepository.save(task);

        // TaskDetailResponse 생성하여 반환
        return TaskDetailResponse.builder()
                .taskId(savedTask.getId())
                .projectId(projectId)
                .title(savedTask.getTitle())
                .content(savedTask.getContent())
                // 마일스톤은 아직 설정하지 않음 -> null로 반환
                .milestone(null)
                // 태그는 아직 설정하지 않음 -> 빈 리스트로 반환
                .tags(List.of())
                .build();
    }

    // 테스크 상세 조회
    @Override
    public TaskDetailResponse getTask(Long projectId, Long taskId, String userId) {
        checkUser(projectId, userId);

        // 테스크 조회
        Task task=taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException(taskId+"번 테스크를 찾을 수 없습니다."));

        // MilestoneResponse 생성
        MilestoneResponse milestone=task.getMilestone()==null?null:
                MilestoneResponse.builder()
                        .milestoneId(task.getMilestone().getId())
                        .name(task.getMilestone().getName())
                        .status(task.getMilestone().getStatus())
                        .build();

        // TagResponse 리스트 생성
        List<TagResponse> tags=task.getTaskTags().stream()
                .map(taskTag -> {
                    Tag tag=taskTag.getTag();

                    return TagResponse.builder()
                            .tagId(tag.getId())
                            .name(tag.getName())
                            .build();
                }).toList();

        // TaskDetailResponse 생성하여 반환
        return TaskDetailResponse.builder()
                .taskId(task.getId())
                .projectId(task.getProject().getId())
                .title(task.getTitle())
                .content(task.getContent())
                .milestone(milestone)
                .tags(tags)
                .build();
    }

    // 테스크 업데이트
    @Override
    @Transactional
    public TaskDetailResponse updateTask(Long projectId, String userId, Long taskId, TaskUpdateRequest req) {
        checkUser(projectId, userId);

        // 테스크 조회
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException(taskId + "번 테스크를 찾을 수 없습니다."));

        // 마일스톤 조회 (milestoneId가 null이 아닌 경우에만 조회)
        Milestone mileStone = null;
        if (req.milestoneId() != null) {
            mileStone = mileStoneRepository.findById(req.milestoneId())
                    .orElseThrow(() -> new ResourceNotFoundException(req.milestoneId() + "번 마일스톤을 찾을 수 없습니다."));
        }

        // 업데이트 대상 TaskTag 리스트 초기화 -> 기존 매핑된 태그 리스트로 시작
        List<TaskTag> updatedTaskTags=new ArrayList<>(task.getTaskTags());

        // 현재 테스크에 매핑된 TaskTag 리스트
        List<TaskTag> existingTaskTags = task.getTaskTags();

        // 요청으로 들어온 태그 ID 리스트 (null인 경우 빈 리스트로 처리)
        List<Long> requestedTagIds = req.tagIds()==null?List.of():req.tagIds();

        // 삭제 대상 걸러내기 -> 현재 매핑된 태그 중, 요청 리스트에 없는 ID를 가진 것들 삭제
        List<TaskTag> tagsToDelete = existingTaskTags.stream()
                .filter(taskTag -> !requestedTagIds.contains(taskTag.getTag().getId()))
                .toList();
        taskTagRepository.deleteAll(tagsToDelete); // 삭제 대상 DB 반영
        updatedTaskTags.removeAll(tagsToDelete); // 업데이트 대상 리스트에서도 삭제 대상 제거

        // 기존에 있던 태그 중, 요청 리스트에도 있는 ID 가져오기 -> 새로 생성 방지
        List<Long> existingTagIds = existingTaskTags.stream()
                .filter(taskTag -> requestedTagIds.contains(taskTag.getTag().getId()))
                .map(taskTag -> taskTag.getTag().getId())
                .toList();

        // 새로 생성 대상 걸러내기 -> 요청 리스트 중, 기존 매핑에 없는 ID를 가진 것들 생성
        List<Long> tagsToCreateIds = requestedTagIds.stream()
                .filter(tagId -> !existingTagIds.contains(tagId))
                .toList();

        // 새로운 태그 매핑 생성
        if (!tagsToCreateIds.isEmpty()) {
            List<Tag> newTags = tagRepository.findAllById(tagsToCreateIds);

            List<TaskTag> newTaskTags = newTags.stream()
                    .map(tag -> TaskTag.builder()
                            .task(task)
                            .tag(tag)
                            .build())
                    .toList();

            taskTagRepository.saveAll(newTaskTags); // 새로운 태그 매핑 DB 반영
            updatedTaskTags.addAll(newTaskTags); // 업데이트 대상 리스트에 새로운 태그 매핑 추가
        }

        // Task 업데이트
        task.update(req.title(), req.content(), mileStone, updatedTaskTags);

        // 업데이트된 마일스톤 정보로 MilestoneResponse 생성
        MilestoneResponse milestone = mileStone == null ? null :
                MilestoneResponse.builder()
                        .milestoneId(mileStone.getId())
                        .name(mileStone.getName())
                        .status(mileStone.getStatus())
                        .build();

        // 업데이트된 태그 매핑 기준으로 TagResponse 리스트 생성
        List<TagResponse> tagResponses = updatedTaskTags.stream()
                .map(taskTag -> {
                    Tag tag = taskTag.getTag();
                    return TagResponse.builder()
                            .tagId(tag.getId())
                            .name(tag.getName())
                            .build();
                })
                .toList();

        // TaskDetailResponse 생성하여 반환
        return TaskDetailResponse.builder()
                .taskId(task.getId())
                .projectId(task.getProject().getId())
                .title(task.getTitle())
                .content(task.getContent())
                .milestone(milestone)
                .tags(tagResponses)
                .build();
    }

    // 테스크 삭제
    @Override
    @Transactional
    public void deleteTask(Long projectId, String userId, Long taskId) {
        checkUser(projectId, userId);

        // 테스크 조회
        Task task=taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException(taskId+"번 테스크를 찾을 수 없습니다."));

        // 테스크에 매핑된 TaskTag 모두 삭제
        taskTagRepository.deleteByTask_Id(task.getId());

        // 테스크에 매핑된 Comment 모두 삭제
        commentRepository.deleteByTask_Id(task.getId());

        // 테스크 삭제
        taskRepository.delete(task);
    }


    // 유저가 프로젝트 멤버인지 확인
    private void checkUser(Long projectId, String userId) {
        if(!projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)) {
            throw new ResourceNotFoundException("프로젝트 멤버가 아닙니다.");
        }
    }
}
