package ru.practicum.shareit.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseTemplate handleDuplicateException(final DuplicateException e) {
        return new ErrorResponseTemplate(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseTemplate handleValidationException(final ValidationException e) {
        return new ErrorResponseTemplate(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseTemplate handleNotFoundException(final NotFoundException e) {
        return new ErrorResponseTemplate(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseTemplate handleInternalError(final ExceptionServer e) {
        return new ErrorResponseTemplate(e.getMessage());
    }
}
