package net.patrykdobrowolski.bookscanner.rest;

import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SessionNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void handleException() {}
}
