package com.nhnacademy.taskapi.exception.allow.ex;

import com.nhnacademy.taskapi.exception.allow.ResourceNotAllowException;

public class MilestoneNotAllowException extends ResourceNotAllowException {

    private static final String Default_Template ="Milestone이 허용되지 않습니다.";
    private static final String ID_Message_Template ="id %d에 해당하는 Milestone이 허용되지 않습니다.";

     public MilestoneNotAllowException() {
         super(Default_Template);
     }

     public MilestoneNotAllowException(Long id) {
         super(String.format(ID_Message_Template, id));
     }

    public MilestoneNotAllowException(String message) {
        super(message);
    }
}
