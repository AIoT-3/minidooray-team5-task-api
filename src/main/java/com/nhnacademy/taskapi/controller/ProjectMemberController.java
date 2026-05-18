package com.nhnacademy.taskapi.controller;

import com.nhnacademy.taskapi.dto.project.ProjectCreateRequest;
import com.nhnacademy.taskapi.dto.project_member.ProjectMemberAddRequest;
import com.nhnacademy.taskapi.dto.project_member.ProjectMemberResponse;
import com.nhnacademy.taskapi.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    // 프로젝트 멤버 전체 조회
    @GetMapping
    public ResponseEntity<List<ProjectMemberResponse>> getProjectMembers(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("X-User-Id") String userId
    ) {
        List<ProjectMemberResponse> members=projectMemberService.getMembers(projectId, userId);

        return ResponseEntity.ok(members);
    }

    // 프로젝트 멤버 추가
    @PostMapping
    public ResponseEntity<ProjectMemberResponse> addProjectMember(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody ProjectMemberAddRequest req
            ) {
        ProjectMemberResponse resp=projectMemberService.addMember(projectId, userId, req.userId());

        return ResponseEntity.ok(resp);
    }

    // 프로젝트 멤버 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeProjectMember(
            @PathVariable("projectId") Long projectId,
            @PathVariable("userId") String memberId,
            @RequestHeader("X-User-Id") String userId
    ) {
        projectMemberService.deleteMember(projectId, userId, memberId);

        return ResponseEntity.noContent().build();
    }

}
