package com.nhnacademy.taskapi.dto.user;

import lombok.Builder;

@Builder
public record UserExistsResponse(
        Boolean exists
) {
}
