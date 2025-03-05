package dev.datpl.commonservice.advice;

import dev.datpl.commonservice.exception.UserCreationException;
import dev.datpl.commonservice.exception.UserDeletionException;
import dev.datpl.commonservice.exception.UserRetrievalException;
import dev.datpl.commonservice.pojo.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CommonControllerAdvice {

    @ExceptionHandler(UserCreationException.class)
    public ErrorResponse handleKeycloakUserCreationException(UserCreationException ex) {
        log.error(ex.getDetailedMessage());
        return
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .status(ex.getStatusCode()).build();
    }


    @ExceptionHandler(UserRetrievalException.class)
    public ErrorResponse handleUserRetrievalException(UserRetrievalException ex) {
        log.error("User Retrieval Error: {}", ex.getMessage());
        return
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }

    @ExceptionHandler(UserDeletionException.class)
    public ErrorResponse handleUserDeletionException(UserDeletionException ex) {
        log.error("User Deletion Error for User ID {}: {}", ex.getUserId(), ex.getMessage());
        return
                ErrorResponse.builder()
                        .message(String.format("User Deletion Error for User ID {}: {}", ex.getUserId(), ex.getMessage()))
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }

}
