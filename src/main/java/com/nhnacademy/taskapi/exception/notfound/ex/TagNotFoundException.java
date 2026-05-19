package com.nhnacademy.taskapi.exception.notfound.ex;

import com.nhnacademy.taskapi.exception.notfound.ResourceNotFoundException;

public class TagNotFoundException extends ResourceNotFoundException {

    private static final String Default_Template ="일치하는 Tag가 없습니다.";
    private static final String ID_Message_Template ="id %d에 해당하는 Tag가 없습니다.";

    public TagNotFoundException() {
        super(Default_Template);
    }

    public TagNotFoundException(Long id) {
        super(String.format(ID_Message_Template, id));
    }

    public TagNotFoundException(String message) {
        super(message);
    }
}
