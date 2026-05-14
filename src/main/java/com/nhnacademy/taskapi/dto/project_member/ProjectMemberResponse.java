package com.nhnacademy.taskapi.dto.project_member;

import lombok.Builder;

@Builder
public record ProjectMemberResponse(
        String userId,
        boolean admin
) {
}
