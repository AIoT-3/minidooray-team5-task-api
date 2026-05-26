package com.nhnacademy.taskapi.exception.notfound.ex;

import com.nhnacademy.taskapi.exception.notfound.ResourceNotFoundException;

public class TaskNotFoundException extends ResourceNotFoundException {

    private static final String Default_Template ="일치하는 Task가 없습니다.";
    private static final String ID_Message_Template ="id %d에 해당하는 Task가 없습니다.";

    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException() {
        super(Default_Template);
    }

    public TaskNotFoundException(Long id) {
        super(String.format(ID_Message_Template, id));
    }
}
