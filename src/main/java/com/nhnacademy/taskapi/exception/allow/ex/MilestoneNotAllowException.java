package com.nhnacademy.taskapi.exception.allow.ex;

import com.nhnacademy.taskapi.exception.allow.ResourceNotAllowException;

public class MilestoneNotAllowException extends ResourceNotAllowException {

    private static final String defaultTemplate="Milestone이 허용되지 않습니다.";
    private static final String idMessageTemplate="id %d에 해당하는 Milestone이 허용되지 않습니다.";

     public MilestoneNotAllowException() {
         super(defaultTemplate);
     }

     public MilestoneNotAllowException(Long id) {
         super(String.format(idMessageTemplate, id));
     }

    public MilestoneNotAllowException(String message) {
        super(message);
    }
}
