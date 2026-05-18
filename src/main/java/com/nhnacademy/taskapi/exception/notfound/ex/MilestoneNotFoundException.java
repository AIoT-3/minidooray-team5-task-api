package com.nhnacademy.taskapi.exception.notfound.ex;

import com.nhnacademy.taskapi.exception.notfound.ResourceNotFoundException;

public class MilestoneNotFoundException extends ResourceNotFoundException {

    private static final String defaultTemplate="일치하는 Milestone이 없습니다.";
    private static final String idMessageTemplate="id %d에 해당하는 Milestone이 없습니다.";

    public MilestoneNotFoundException() {
        super(defaultTemplate);
    }

    public MilestoneNotFoundException(Long id) {
        super(String.format(idMessageTemplate, id));
    }

    public MilestoneNotFoundException(String message) {
        super(message);
    }
}
