package dev.datpl.commonservice.exception;

import lombok.Getter;

@Getter
public class UserDeletionException extends RuntimeException {

    String userId;

    public UserDeletionException(String message, String userId) {
        super(message);
        this.userId = userId;
    }
}
