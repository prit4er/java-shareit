package ru.practicum.shareit.exeptions;

public class BookingUpdateStatusException extends RuntimeException {

    public BookingUpdateStatusException(String message) {
        super(message);
    }
}
