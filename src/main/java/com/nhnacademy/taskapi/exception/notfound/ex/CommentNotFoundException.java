package com.nhnacademy.taskapi.exception.notfound.ex;

import com.nhnacademy.taskapi.exception.notfound.ResourceNotFoundException;

public class CommentNotFoundException extends ResourceNotFoundException {

     private static final String Default_Template="일치하는 Comment가 없습니다.";
     private static final String ID_Message_Template ="id %d에 해당하는 Comment가 없습니다.";

     public CommentNotFoundException() {
         super(Default_Template);
     }

     public CommentNotFoundException(Long id) {
         super(String.format(ID_Message_Template, id));
     }
    public CommentNotFoundException(String message) {
        super(message);
    }
}
