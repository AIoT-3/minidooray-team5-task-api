package com.nhnacademy.taskapi.exception;

public class ResourceNotAllowException extends RuntimeException {
    public ResourceNotAllowException(String message) {
        super(message);
    }
}
