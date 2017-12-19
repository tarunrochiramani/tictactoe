package com.tr.exception;

public class InvalidMoveException extends Exception {
    private String message = "Invalid Move - ";

    public InvalidMoveException(String additionalMessage) {
        message = message + additionalMessage;
    }

    public String getMessage() {
        return message;
    }
}
