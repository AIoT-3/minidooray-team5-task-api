package com.nhnacademy.taskapi.controller;

import com.nhnacademy.taskapi.dto.milestone.MilestoneCreateRequest;
import com.nhnacademy.taskapi.dto.milestone.MilestoneResponse;
import com.nhnacademy.taskapi.dto.milestone.MilestoneUpdateRequest;
import com.nhnacademy.taskapi.service.MilestoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/milestones")
public class MilestoneController {

    private final MilestoneService milestoneService;

    // 마일스톤 목록 조회
    @GetMapping
    public ResponseEntity<List<MilestoneResponse>> getMilestonesByProject(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("X-User-Id") String userId
    ) {
        List<MilestoneResponse> resp=milestoneService.getMilestoneList(projectId, userId);

        return ResponseEntity.ok(resp);
    }

    // 마일스톤 생성
    @PostMapping
    public ResponseEntity<MilestoneResponse> createMilestone(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody MilestoneCreateRequest req
    ) {
        MilestoneResponse resp=milestoneService.createMilestone(projectId, userId, req);

        return ResponseEntity.ok(resp);
    }

    // 마일스톤 단건 조회
    @GetMapping("/{milestoneId}")
    public ResponseEntity<MilestoneResponse> getMilestone(
            @PathVariable("projectId") Long projectId,
            @PathVariable("milestoneId") Long milestoneId,
            @RequestHeader("X-User-Id") String userId
    ) {
        MilestoneResponse resp=milestoneService.getMilestone(projectId, userId, milestoneId);

        return ResponseEntity.ok(resp);
    }

    // 마일스톤 수정
    @PostMapping("/{milestoneId}")
    public ResponseEntity<MilestoneResponse> updateMilestone(
            @PathVariable("projectId") Long projectId,
            @PathVariable("milestoneId") Long milestoneId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody MilestoneUpdateRequest req
            ) {
        MilestoneResponse resp=milestoneService.updateMilestone(projectId, userId, milestoneId, req);

        return ResponseEntity.ok(resp);
    }

    // 마일스톤 삭제
    @DeleteMapping("/{milestoneId}")
    public ResponseEntity<Void> deleteMilestone(
            @PathVariable("projectId") Long projectId,
            @PathVariable("milestoneId") Long milestoneId,
            @RequestHeader("X-User-Id") String userId
    ) {
        milestoneService.deleteMilestone(projectId, userId, milestoneId);

        return ResponseEntity.noContent().build();
    }
}
