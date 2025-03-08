package ru.practicum.shareit.exeptions;

public class UserNotValidToCommentException extends RuntimeException {

    public UserNotValidToCommentException(String message) {
        super(message);
    }
}
