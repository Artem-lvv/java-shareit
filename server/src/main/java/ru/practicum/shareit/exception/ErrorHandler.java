package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleUnsupportedStatus(UnsupportedStatusException e) {
        log.debug("Exception received UnsupportedStatusException 400 Bad request {}", e.getMessage(), e);
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public void handleServerException(Throwable e) {
        log.debug("Exception received Throwable 500 Internal server error {}", e.getMessage(), e);
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ExceptionHandler
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.debug("Exception received MethodArgumentNotValidException 400 Bad request {}", e.getMessage(), e);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity arguments failed validation");
    }

}
