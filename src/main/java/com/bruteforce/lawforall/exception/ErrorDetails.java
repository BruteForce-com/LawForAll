package com.bruteforce.lawforall.exception;

import java.time.LocalDateTime;

public record ErrorDetails(LocalDateTime timestamp, String  message) {

    // Getters for timestamp, message, and details
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String  getMessage() {
        return message;
    }

}
