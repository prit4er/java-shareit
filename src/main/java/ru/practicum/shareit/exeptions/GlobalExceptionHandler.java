package ru.practicum.shareit.exeptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error("NotFoundException was thrown");
        return new ErrorResponse("Search was failed", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(final AccessDeniedException e) {
        log.error("AccessDeniedException was thrown");
        return new ErrorResponse("Access exception", e.getMessage());
    }

    @ExceptionHandler({BookingUpdateStatusException.class,
            BookingDeniedException.class,
            UserNotValidToCommentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final RuntimeException e) {
        log.error("BookingDeniedException was thrown");
        return new ErrorResponse("Something went wrong with booking", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherExceptions(final Exception e) {
        log.error("Exception was thrown");
        return new ErrorResponse("Something went wrong", e.getMessage());
    }
}