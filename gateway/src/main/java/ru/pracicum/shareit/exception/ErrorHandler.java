package ru.pracicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.debug("Exception received MethodArgumentNotValidException 400 Bad request {}", e.getMessage(), e);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity arguments failed validation");
    }
}
