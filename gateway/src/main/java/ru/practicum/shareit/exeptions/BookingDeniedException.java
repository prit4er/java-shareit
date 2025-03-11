package ru.practicum.shareit.exeptions;

public class BookingDeniedException extends RuntimeException {

    public BookingDeniedException(String message) {
        super(message);
    }
}
