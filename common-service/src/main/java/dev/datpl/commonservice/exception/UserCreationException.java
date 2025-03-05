package dev.datpl.commonservice.exception;

import lombok.Getter;

@Getter
public class UserCreationException extends RuntimeException {

    int statusCode;

    public UserCreationException(String message, int statusCode) {
        super(message + " with status code " + statusCode);
        this.statusCode = statusCode;
    }

    public String getDetailedMessage() {
        return String.format("User creation failed with status code %d: %s",
                statusCode, getErrorDescription(statusCode));
    }

    private String getErrorDescription(int statusCode) {
        return switch (statusCode) {
            case 400 -> "Bad Request - Invalid user data";
            case 401 -> "Unauthorized - Authentication failed";
            case 403 -> "Forbidden - Insufficient permissions";
            case 409 -> "Conflict - User already exists";
            default -> "Unknown error occurred during user creation";
        };
    }
}
