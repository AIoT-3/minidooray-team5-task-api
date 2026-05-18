package com.nhnacademy.taskapi.exception.allow.ex;

import com.nhnacademy.taskapi.exception.allow.ResourceNotAllowException;

public class CommentNotAllowException extends ResourceNotAllowException {

    private static final String defaultTemplate="댓글 작성이 허용되지 않습니다.";
    private static final String IdMessageTemplate="id %d에 해당하는 댓글 작성이 허용되지 않습니다.";

    public CommentNotAllowException() {
        super(defaultTemplate);
    }

    public CommentNotAllowException(Long id) {
        super(String.format(IdMessageTemplate, id));
    }

    public CommentNotAllowException(String message) {
        super(message);
    }
}
