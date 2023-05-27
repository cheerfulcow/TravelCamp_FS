package com.example.travelcamp.exception;

public class UserNotFoundException extends RuntimeException {

    private final String additionalMessage;

    public UserNotFoundException(String message, String additionalMessage) {
        super(message);
        this.additionalMessage = additionalMessage;
    }

    public String getAdditionalMessage() {
        return additionalMessage;
    }
}
