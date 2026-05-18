package com.nhnacademy.taskapi.exception.allow;

public class ResourceNotAllowException extends RuntimeException {
    public ResourceNotAllowException(String message) {
        super(message);
    }
}
