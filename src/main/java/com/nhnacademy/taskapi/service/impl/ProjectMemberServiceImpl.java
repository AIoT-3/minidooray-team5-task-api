package com.nhnacademy.taskapi.service.impl;

import com.nhnacademy.taskapi.dto.project_member.ProjectMemberResponse;
import com.nhnacademy.taskapi.entity.ProjectMember;
import com.nhnacademy.taskapi.exception.ResourceNotAllowException;
import com.nhnacademy.taskapi.exception.ResourceNotFoundException;
import com.nhnacademy.taskapi.repository.ProjectMemberRepository;
import com.nhnacademy.taskapi.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;

    // 프로젝트 멤버 조회
    @Override
    public List<ProjectMemberResponse> getMembers(Long projectId, String userId) {
        // 프로젝트 멤버 조회
        List<ProjectMember> members=projectMemberRepository.findAllByProject_Id(projectId);

        // 조회하는 유저가 프로젝트 멤버인지 확인
        if(members.stream().noneMatch(m->m.getUserId().equals(userId))) {
            throw new ResourceNotAllowException("프로젝트 멤버가 아닙니다.");
        }

        // 응답 반환 ProjectMember -> ProjectMemberResponse
        return members.stream().map(m->ProjectMemberResponse.builder()
                .userId(m.getUserId())
                .admin(m.isAdmin())
                .build()
        ).collect(Collectors.toList());
    }

    // 프로젝트 멤버 추가
    @Override
    @Transactional
    public ProjectMemberResponse addMember(Long projectId, String userId, String newMemberId) {
        // 프로젝트 멤버 조회 -> 추가하는 유저가 프로젝트 멤버인지 확인
        ProjectMember projectmember=projectMemberRepository.findByProject_IdAndUserId(projectId, userId)
                .orElseThrow(()->new ResourceNotFoundException("프로젝트 멤버가 아닙니다."));

        // 조회하는 유저가 프로젝트 멤버인지 확인 -> 조회하는 유저가 프로젝트 관리자(admin)인지 확인
        if(!projectmember.isAdmin()) {
            throw new ResourceNotAllowException("프로젝트 관리자만 멤버를 추가할 수 있습니다.");
        }

        // 멤버 추가 -> 프로젝트 멤버 생성
        ProjectMember member=projectMemberRepository.save(
                ProjectMember.builder()
                        .project(projectmember.getProject())
                        .userId(newMemberId)
                        .admin(false)
                        .build()
        );

        // 응답 반환
        return ProjectMemberResponse.builder()
                .userId(member.getUserId())
                .admin(member.isAdmin())
                .build();
    }

    // 프로젝트 멤버 삭제
    @Override
    @Transactional
    public void deleteMember(Long projectId, String userId, String memberId) {
        // 프로젝트 멤버 조회 -> 삭제 요청을 하는 유저가 프로젝트 멤버인지 확인
        ProjectMember projectmember=projectMemberRepository.findByProject_IdAndUserId(projectId, userId)
                .orElseThrow(()->new ResourceNotFoundException("프로젝트 멤버가 아닙니다."));

        // 삭제 요청을 하는 유저가 프로젝트 관리자 or 본인인지 확인
        if(!projectmember.isAdmin()&&!projectmember.getUserId().equals(memberId)) {
            throw new ResourceNotAllowException("프로젝트 관리자나 본인만 삭제할 수 있습니다.");
        }

        // 삭제할 멤버 조회 -> 삭제할 멤버가 프로젝트 멤버인지 확인
        ProjectMember member=projectMemberRepository.findByProject_IdAndUserId(projectId, memberId)
                .orElseThrow(()->new ResourceNotFoundException("삭제할 멤버가 프로젝트 멤버가 아닙니다."));

        // 멤버 삭제
        projectMemberRepository.delete(member);
    }
}
