package com.nhnacademy.taskapi.controller;

import com.nhnacademy.taskapi.dto.milestone.MilestoneCreateRequest;
import com.nhnacademy.taskapi.dto.milestone.MilestoneResponse;
import com.nhnacademy.taskapi.dto.milestone.MilestoneUpdateRequest;
import com.nhnacademy.taskapi.repository.MileStoneRepository;
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

    private final MileStoneRepository mileStoneRepository;

    @GetMapping
    public ResponseEntity<List<MilestoneResponse>> getMilestonesByProject(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("X-User-Id") String userId
    ) {

        return null;
    }

    @PostMapping
    public ResponseEntity<MilestoneResponse> createMilestone(
            @PathVariable("projectId") Long projectId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody MilestoneCreateRequest req
    ) {

        return null;
    }

    @GetMapping("/{milestoneId}")
    public ResponseEntity<MilestoneResponse> getMilestone(
            @PathVariable("projectId") Long projectId,
            @PathVariable("milestoneId") Long milestoneId,
            @RequestHeader("X-User-Id") String userId
    ) {

        return null;
    }

    @PostMapping("/{milestoneId}")
    public ResponseEntity<MilestoneResponse> updateMilestone(
            @PathVariable("projectId") Long projectId,
            @PathVariable("milestoneId") Long milestoneId,
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody MilestoneUpdateRequest req
            ) {

        return null;
    }

    @DeleteMapping("/{milestoneId}")
    public ResponseEntity<Void> deleteMilestone(
            @PathVariable("projectId") Long projectId,
            @PathVariable("milestoneId") Long milestoneId,
            @RequestHeader("X-User-Id") String userId
    ) {

        return null;
    }
}
