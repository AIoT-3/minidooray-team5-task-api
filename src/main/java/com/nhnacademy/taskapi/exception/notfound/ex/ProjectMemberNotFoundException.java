package com.nhnacademy.taskapi.exception.notfound.ex;

import com.nhnacademy.taskapi.exception.notfound.ResourceNotFoundException;

public class ProjectMemberNotFoundException extends ResourceNotFoundException {

    private static final String Default_Template ="일치하는 ProjectMember가 없습니다.";
    private static final String ID_Message_Template ="id %d에 해당하는 ProjectMember가 없습니다.";

    public ProjectMemberNotFoundException() {
        super(Default_Template);
    }

    public ProjectMemberNotFoundException(Long id) {
        super(String.format(ID_Message_Template, id));
    }

    public ProjectMemberNotFoundException(String message) {
        super(message);
    }
}
