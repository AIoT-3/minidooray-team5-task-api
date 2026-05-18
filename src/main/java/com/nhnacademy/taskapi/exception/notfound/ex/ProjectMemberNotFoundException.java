package com.nhnacademy.taskapi.exception.notfound.ex;

import com.nhnacademy.taskapi.exception.notfound.ResourceNotFoundException;

public class ProjectMemberNotFoundException extends ResourceNotFoundException {

    private static final String defaultTemplate="일치하는 ProjectMember가 없습니다.";
    private static final String IdMessageTemplate="id %d에 해당하는 ProjectMember가 없습니다.";

    public ProjectMemberNotFoundException() {
        super(defaultTemplate);
    }

    public ProjectMemberNotFoundException(Long id) {
        super(String.format(IdMessageTemplate, id));
    }

    public ProjectMemberNotFoundException(String message) {
        super(message);
    }
}
