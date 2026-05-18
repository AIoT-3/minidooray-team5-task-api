package com.nhnacademy.taskapi.exception.allow.ex;

import com.nhnacademy.taskapi.exception.allow.ResourceNotAllowException;

public class ProjectNotAllowException extends ResourceNotAllowException {

    private static final String defaultTemplate="해당 Project는 허용되지 않습니다.";
    private static final String IdMessageTemplate="id %d에 해당하는 Project는 허용되지 않습니다.";

    public ProjectNotAllowException() {
        super(defaultTemplate);
    }

    public ProjectNotAllowException(Long id) {
        super(String.format(IdMessageTemplate, id));
    }

    public ProjectNotAllowException(String message) {
        super(message);
    }
}
