package com.nhnacademy.taskapi.dto.tag;

import lombok.Builder;

@Builder
public record TagResponse(
        Long tagId,
        String name
) {
}
