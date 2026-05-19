package com.nhnacademy.taskapi.exception.notfound.ex;

import com.nhnacademy.taskapi.exception.notfound.ResourceNotFoundException;

public class ProjectNotFoundException extends ResourceNotFoundException {

    private static final String Default_Template ="일치하는 Project가 없습니다.";
    private static final String ID_Message_Template ="id %d에 해당하는 Project가 없습니다.";

    public ProjectNotFoundException(String message) {
        super(message);
    }

    public ProjectNotFoundException() {
        super(Default_Template);
    }

    public ProjectNotFoundException(Long id) {
        super(String.format(ID_Message_Template, id));
    }
}
