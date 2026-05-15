package com.nhnacademy.taskapi.service;

import com.nhnacademy.taskapi.dto.project_member.ProjectMemberResponse;

import java.util.List;

public interface ProjectMemberService {
    List<ProjectMemberResponse> getMembers(Long projectId, String userId);

    ProjectMemberResponse addMember(Long projectId, String userId, String newMemberId);

    void deleteMember(Long projectId, String userId, String memberId);
}
