package com.nhnacademy.taskapi.exception.notfound.ex;

import com.nhnacademy.taskapi.exception.notfound.ResourceNotFoundException;

public class CommentNotFoundException extends ResourceNotFoundException {

     private static final String defaultTemplate="일치하는 Comment가 없습니다.";
     private static final String idMessageTemplate="id %d에 해당하는 Comment가 없습니다.";

     public CommentNotFoundException() {
         super(defaultTemplate);
     }

     public CommentNotFoundException(Long id) {
         super(String.format(idMessageTemplate, id));
     }
    public CommentNotFoundException(String message) {
        super(message);
    }
}
