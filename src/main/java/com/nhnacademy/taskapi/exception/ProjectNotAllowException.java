package com.nhnacademy.taskapi.exception;

public class ProjectNotAllowException extends RuntimeException {
    public ProjectNotAllowException(String message) {
        super(message);
    }
}
