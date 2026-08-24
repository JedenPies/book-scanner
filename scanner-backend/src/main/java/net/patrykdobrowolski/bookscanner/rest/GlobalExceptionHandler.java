package net.patrykdobrowolski.bookscanner.rest;

import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.ShareCodeGenerationException;
import net.patrykdobrowolski.bookscanner.domain.exception.ShareCodeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SessionNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void handleException() {}

    @ExceptionHandler(ShareCodeGenerationException.class)
    @ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
    public void handleShareCodeGenerationException() {}

    @ExceptionHandler(ShareCodeNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void handleShareCodeNotFoundException() {}

}
