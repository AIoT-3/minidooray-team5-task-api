package com.nhnacademy.taskapi.exception.notfound.ex;

import com.nhnacademy.taskapi.exception.notfound.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
