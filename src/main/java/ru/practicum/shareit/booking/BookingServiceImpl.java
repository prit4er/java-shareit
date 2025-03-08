package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exeptions.AccessDeniedException;
import ru.practicum.shareit.exeptions.BookingDeniedException;
import ru.practicum.shareit.exeptions.BookingUpdateStatusException;
import ru.practicum.shareit.exeptions.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.booking.BookingDtoMapper.toEntity;
import static ru.practicum.shareit.booking.BookingDtoMapper.toResponseDto;
import static ru.practicum.shareit.booking.BookingStatus.WAITING;
import static ru.practicum.shareit.user.UserDtoMapper.toEntity;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookingResponseDto create(Integer userId, BookingDto bookingDto) {
        log.trace("Adding booking at service level has started");

        User user = toEntity(userService.getById(userId));
        log.debug("User with id: {} is in repository", userId);

        Item item = itemRepository.findById(bookingDto.getItemId())
                                  .orElseThrow(() -> new NotFoundException(
                                          String.format("There's no item with id: %d in repository", bookingDto.getItemId())));
        log.debug("Item with id: {} is in repository", item.getId());

        validateItemAvailability(item, userId);  // Проверка доступности элемента

        Booking booking = toEntity(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(WAITING);
        log.debug("The booking entity has been created");

        // Логирование данных перед сохранением
        log.debug("Booking details: {}", booking);

        Booking savedBooking = bookingRepository.save(booking);
        log.debug("Booking saved with id: {}", savedBooking.getId());

        return toResponseDto(savedBooking);
    }

    @Override
    public BookingResponseDto findById(Integer userId, Integer bookingId) {
        log.trace("Searching for booking with id: {} by user with id {} has started (at service layer)", bookingId, userId);

        userService.getById(userId);
        log.debug("User with id: {} is in repository", userId);

        Booking booking = bookingRepository.findById(bookingId)
                                           .orElseThrow(() -> new NotFoundException(
                                                   String.format("There's no booking with id: %d in repository", bookingId)));
        log.debug("Booking with id: {} is in repository", bookingId);

        validateAccess(userId, booking);
        log.debug("The search of the booking with id {} by user with id {} was succeeded", bookingId, userId);

        return toResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> getOwnerBookings(Integer userId, BookingStatus state) {
        log.trace("Searching for owner bookings with id: {} has started (at service layer)", userId);

        userService.getById(userId);
        log.debug("User with id: {} is in repository", userId);

        LocalDateTime now = LocalDateTime.now();
        log.trace("Current Date-Time set: {}", now);

        List<Booking> bookings = switch (state) {
            case CURRENT -> bookingRepository.findBookingsByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
            case PAST -> bookingRepository.findBookingsByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, now);
            case FUTURE -> bookingRepository.findBookingsByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now);
            case WAITING -> bookingRepository.findBookingsByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findBookingsByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            default -> bookingRepository.findBookingsByItemOwnerIdOrderByStartDesc(userId);
        };
        log.debug("Owner booking status set");

        return bookings.stream().map(BookingDtoMapper::toResponseDto).toList();
    }

    @Override
    public List<BookingResponseDto> getUserBookings(Integer userId, BookingStatus state) {
        log.trace("Searching for user bookings with id: {} has started (at service layer)", userId);

        userService.getById(userId);
        log.debug("User with id: {} is in repository", userId);

        LocalDateTime now = LocalDateTime.now();
        log.trace("Current Date-Time set: {}", now);

        List<Booking> bookings = switch (state) {
            case CURRENT -> bookingRepository.findBookingsByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
            case PAST -> bookingRepository.findBookingsByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
            case FUTURE -> bookingRepository.findBookingsByBookerIdAndStartAfterOrderByStartDesc(userId, now);
            case WAITING -> bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            default -> bookingRepository.findBookingsByBookerIdOrderByStartDesc(userId);
        };
        log.debug("User booking status set");

        return bookings.stream().map(BookingDtoMapper::toResponseDto).toList();
    }

    @Override
    public BookingResponseDto updateStatus(Integer userId, Integer bookingId, Boolean approved) {
        log.trace("Status update of booking with id: {} has started (at service layer)", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                                           .orElseThrow(() -> new NotFoundException(
                                                   String.format("There's no booking with id: %d in repository", bookingId)));
        log.debug("Booking with id: {} is in repository", bookingId);

        // Проверка, что бронирование не было уже утверждено
        if (booking.getStatus().equals(BookingStatus.APPROVED) && approved) {
            log.warn("Booking with id: {} is already approved. Cannot approve again.", bookingId);
            throw new BookingUpdateStatusException(
                    String.format("Unable to approve the booking. Booking with id: %d has already been approved", bookingId));
        }

        // Проверка прав владельца
        validateOwnerAccess(userId, booking);

        // Установка нового статуса
        BookingStatus newStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(newStatus);
        log.debug("Status updated to {}", newStatus);

        // Сохранение изменения и возврат DTO
        Booking savedBooking = bookingRepository.save(booking);
        log.debug("Booking status successfully updated and saved with id: {}", savedBooking.getId());

        return toResponseDto(savedBooking);
    }


    private void validateItemAvailability(Item item, Integer userId) {
        if (!item.getAvailable()) {
            log.error("Item with id: {} is not available for booking", item.getId());
            throw new BookingDeniedException("Item is not available for booking");
        }
    }

    private void validateAccess(Integer userId, Booking booking) {
        if (!Objects.equals(userId, booking.getBooker().getId()) &&
                !Objects.equals(userId, booking.getItem().getOwner().getId())) {
            throw new AccessDeniedException("Only the owner of the item or the author of the booking have access");
        }
    }

    private void validateOwnerAccess(Integer userId, Booking booking) {
        if (!Objects.equals(userId, booking.getItem().getOwner().getId())) {
            throw new AccessDeniedException(
                    String.format("Status update denied. User with id: %d isn't an owner of item with id: %d",
                                  userId, booking.getItem().getId()));
        }
    }
}