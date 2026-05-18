package com.nhnacademy.taskapi.exception.notfound.ex;

import com.nhnacademy.taskapi.exception.notfound.ResourceNotFoundException;

public class TagNotFoundException extends ResourceNotFoundException {

    private static final String defaultTemplate="일치하는 Tag가 없습니다.";
    private static final String IdMessageTemplate="id %d에 해당하는 Tag가 없습니다.";

    public TagNotFoundException() {
        super(defaultTemplate);
    }

    public TagNotFoundException(Long id) {
        super(String.format(IdMessageTemplate, id));
    }

    public TagNotFoundException(String message) {
        super(message);
    }
}
