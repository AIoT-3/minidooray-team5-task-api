package com.nhnacademy.taskapi.exception.notfound.ex;

import com.nhnacademy.taskapi.exception.notfound.ResourceNotFoundException;

public class TaskNotFoundException extends ResourceNotFoundException {

    private static final String defaultTemplate="일치하는 Task가 없습니다.";
    private static final String idMessageTemplate="id %d에 해당하는 Task가 없습니다.";

    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException() {
        super(defaultTemplate);
    }

    public TaskNotFoundException(Long id) {
        super(String.format(idMessageTemplate, id));
    }
}
