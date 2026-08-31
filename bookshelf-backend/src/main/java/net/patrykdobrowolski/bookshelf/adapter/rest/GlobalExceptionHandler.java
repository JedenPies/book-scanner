package net.patrykdobrowolski.bookshelf.adapter.rest;

import net.patrykdobrowolski.bookshelf.domain.exception.*;
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

    @ExceptionHandler(ExportAlreadyRequestedException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public void handleExportAlreadyRequestedException() {}

    @ExceptionHandler(ExportNotRequestedException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void exportNotRequestedException() {}

}
