package ru.practicum.shareit.exeptions;

public class UserAccessDeniedException extends RuntimeException {

    public UserAccessDeniedException(String message) {
        super(message);
    }
}
