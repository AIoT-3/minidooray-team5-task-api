package com.nhnacademy.taskapi.exception.allow.ex;

import com.nhnacademy.taskapi.exception.allow.ResourceNotAllowException;

public class CommentNotAllowException extends ResourceNotAllowException {

    private static final String Default_Template="댓글 작성이 허용되지 않습니다.";
    private static final String ID_Message_Template="id %d에 해당하는 댓글 작성이 허용되지 않습니다.";

    public CommentNotAllowException() {
        super(Default_Template);
    }

    public CommentNotAllowException(Long id) {
        super(String.format(ID_Message_Template, id));
    }

    public CommentNotAllowException(String message) {
        super(message);
    }
}
