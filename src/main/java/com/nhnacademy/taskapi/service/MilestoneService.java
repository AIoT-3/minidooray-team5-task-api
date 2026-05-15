package com.nhnacademy.taskapi.service;

import com.nhnacademy.taskapi.dto.milestone.MilestoneCreateRequest;
import com.nhnacademy.taskapi.dto.milestone.MilestoneResponse;
import com.nhnacademy.taskapi.dto.milestone.MilestoneUpdateRequest;

import java.util.List;

public interface MilestoneService {
    List<MilestoneResponse> getMilestoneList(Long projectId, String userId);

    MilestoneResponse createMilestone(Long projectId, String userId, MilestoneCreateRequest req);

    MilestoneResponse getMilestone(Long projectId, String userId, Long milestoneId);

    MilestoneResponse updateMilestone(Long projectId, String userId, Long milestoneId, MilestoneUpdateRequest req);

    void deleteMilestone(Long projectId, String userId, Long milestoneId);
}
