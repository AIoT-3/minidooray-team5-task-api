package com.nhnacademy.taskapi.service.impl;

import com.nhnacademy.taskapi.dto.milestone.MilestoneCreateRequest;
import com.nhnacademy.taskapi.dto.milestone.MilestoneResponse;
import com.nhnacademy.taskapi.dto.milestone.MilestoneUpdateRequest;
import com.nhnacademy.taskapi.entity.Milestone;
import com.nhnacademy.taskapi.entity.MilestoneStatus;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.exception.ResourceNotAllowException;
import com.nhnacademy.taskapi.exception.ResourceNotFoundException;
import com.nhnacademy.taskapi.repository.MilestoneRepository;
import com.nhnacademy.taskapi.repository.ProjectMemberRepository;
import com.nhnacademy.taskapi.repository.ProjectRepository;
import com.nhnacademy.taskapi.service.MilestoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MilestoneServiceImpl implements MilestoneService {

    private final ProjectRepository projectRepository;
    private final MilestoneRepository mileStoneRepository;
    private final ProjectMemberRepository projectMemberRepository;

    // 마일스톤 목록 조회
    @Override
    public List<MilestoneResponse> getMilestoneList(Long projectId, String userId) {
        checkUser(projectId, userId);

        // 프로젝트 내 모든 마일스톤 조회
        List<Milestone> milestones=mileStoneRepository.findAllByProject_Id(projectId);

        // 응답 반환 Milestone -> MilestoneResponse
        return milestones.stream().map(
                m -> MilestoneResponse.builder()
                        .milestoneId(m.getId())
                        .name(m.getName())
                        .status(m.getStatus())
                        .build()
        ).toList();
    }

    // 마일스톤 생성
    @Override
    @Transactional
    public MilestoneResponse createMilestone(Long projectId, String userId, MilestoneCreateRequest req) {
        checkUser(projectId, userId);

        // 마일스톤 생성
        Milestone milestone=Milestone.builder()
                .name(req.name())
                .status(MilestoneStatus.OPEN)
                .build();

        // 프로젝트 조회
        Project project=projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(projectId+"프로젝트를 찾을 수 없습니다."));

        // 마일스톤에 프로젝트 설정
        milestone.setProject(project);

        // 마일스톤 저장
        Milestone savedMilestone=mileStoneRepository.save(milestone);

        // 응답 반환 Milestone -> MilestoneResponse
        return MilestoneResponse.builder()
                .milestoneId(savedMilestone.getId())
                .name(savedMilestone.getName())
                .status(savedMilestone.getStatus())
                .build();
    }

    // 마일스톤 단건 조회
    @Override
    public MilestoneResponse getMilestone(Long projectId, String userId, Long milestoneId) {
        checkUser(projectId, userId);

        // 마일스톤 조회
        Milestone milestone=mileStoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException(milestoneId+"마일스톤을 찾을 수 없습니다."));

        // 마일스톤이 해당 프로젝트에 속해있는지 확인
        if(!Objects.equals(milestone.getProject().getId(), projectId)) {
            throw new ResourceNotAllowException("해당 마일스톤은 해당 프로젝트에 속해있지 않습니다.");
        }

        // 응답 반환 Milestone -> MilestoneResponse
        return MilestoneResponse.builder()
                .milestoneId(milestone.getId())
                .name(milestone.getName())
                .status(milestone.getStatus())
                .build();
    }

    // 마일스톤 수정
    @Override
    @Transactional
    public MilestoneResponse updateMilestone(Long projectId, String userId, Long milestoneId, MilestoneUpdateRequest req) {
        checkUser(projectId, userId);

        // 마일스톤 조회
        Milestone milestone=mileStoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException(milestoneId+"마일스톤을 찾을 수 없습니다."));

        // 마일스톤이 해당 프로젝트에 속해있는지 확인
        if(!Objects.equals(milestone.getProject().getId(), projectId)) {
            throw new ResourceNotAllowException("해당 마일스톤은 해당 프로젝트에 속해있지 않습니다.");
        }

        // 마일스톤 업데이트
        milestone.update(req.name(), req.status());

        // 응답 반환 Milestone -> MilestoneResponse
        return MilestoneResponse.builder()
                .milestoneId(milestone.getId())
                .name(milestone.getName())
                .status(milestone.getStatus())
                .build();
    }

    // 마일스톤 삭제
    @Override
    @Transactional
    public void deleteMilestone(Long projectId, String userId, Long milestoneId) {
        checkUser(projectId, userId);

        // 마일스톤 조회
        Milestone milestone=mileStoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException(milestoneId+"마일스톤을 찾을 수 없습니다."));

        // 마일스톤이 해당 프로젝트에 속해있는지 확인
        if(!Objects.equals(milestone.getProject().getId(), projectId)) {
            throw new ResourceNotAllowException("해당 마일스톤은 해당 프로젝트에 속해있지 않습니다.");
        }

        // 마일스톤 삭제
        mileStoneRepository.delete(milestone);
    }

    // 유저가 프로젝트 멤버인지 확인
    private void checkUser(Long projectId, String userId) {
        if(!projectMemberRepository.existsByProject_IdAndUserId(projectId, userId)) {
            throw new ResourceNotFoundException("프로젝트 멤버가 아닙니다.");
        }
    }
}
