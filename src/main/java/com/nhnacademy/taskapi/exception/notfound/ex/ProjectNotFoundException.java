package com.nhnacademy.taskapi.exception.notfound.ex;

import com.nhnacademy.taskapi.exception.notfound.ResourceNotFoundException;

public class ProjectNotFoundException extends ResourceNotFoundException {

    private static final String defaultTemplate="일치하는 Project가 없습니다.";
    private static final String IdMessageTemplate="id %d에 해당하는 Project가 없습니다.";

    public ProjectNotFoundException(String message) {
        super(message);
    }

    public ProjectNotFoundException() {
        super(defaultTemplate);
    }

    public ProjectNotFoundException(Long id) {
        super(String.format(IdMessageTemplate, id));
    }
}
