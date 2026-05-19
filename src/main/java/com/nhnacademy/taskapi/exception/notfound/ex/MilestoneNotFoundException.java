package com.nhnacademy.taskapi.exception.notfound.ex;

import com.nhnacademy.taskapi.exception.notfound.ResourceNotFoundException;

public class MilestoneNotFoundException extends ResourceNotFoundException {

    private static final String Default_Template ="일치하는 Milestone이 없습니다.";
    private static final String ID_Message_Template="id %d에 해당하는 Milestone이 없습니다.";

    public MilestoneNotFoundException() {
        super(Default_Template);
    }

    public MilestoneNotFoundException(Long id) {
        super(String.format(ID_Message_Template, id));
    }

    public MilestoneNotFoundException(String message) {
        super(message);
    }
}
