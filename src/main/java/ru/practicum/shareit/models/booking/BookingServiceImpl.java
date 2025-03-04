package ru.practicum.shareit.models.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeptions.AccessDeniedException;
import ru.practicum.shareit.exeptions.BookingDeniedException;
import ru.practicum.shareit.exeptions.BookingUpdateStatusException;
import ru.practicum.shareit.exeptions.NotFoundException;
import ru.practicum.shareit.models.booking.dto.BookingDto;
import ru.practicum.shareit.models.booking.dto.BookingResponseDto;
import ru.practicum.shareit.models.item.Item;
import ru.practicum.shareit.models.item.ItemRepository;
import ru.practicum.shareit.models.user.User;
import ru.practicum.shareit.models.user.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.models.booking.BookingDtoMapper.toBookingResponse;
import static ru.practicum.shareit.models.booking.BookingDtoMapper.toEntity;
import static ru.practicum.shareit.models.booking.BookingStatus.APPROVED;
import static ru.practicum.shareit.models.booking.BookingStatus.REJECTED;
import static ru.practicum.shareit.models.booking.BookingStatus.WAITING;
import static ru.practicum.shareit.models.user.UserDtoMapper.toEntity;


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
        booking.setStatus(BookingStatus.WAITING);
        log.debug("The booking entity has been created");

        // Логирование данных перед сохранением
        log.debug("Booking details: {}", booking);

        Booking savedBooking = bookingRepository.save(booking);
        log.debug("Booking saved with id: {}", savedBooking.getId());

        return toBookingResponse(savedBooking);
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

        return toBookingResponse(booking);
    }

    @Override
    public Collection<BookingResponseDto> readBookingsForOwner(Integer userId, String state) {
        return readBookings(userId, state, true);
    }

    @Override
    public List<BookingResponseDto> readBookingsForUser(Integer userId, String state) {
        return readBookings(userId, state, false);
    }

    @Override
    public BookingResponseDto updateStatus(Integer userId, Integer bookingId, Boolean approved) {
        log.trace("Status update of booking with id: {} has started (at service layer)", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                                           .orElseThrow(() -> new NotFoundException(
                                                   String.format("There's no booking with id: %d in repository", bookingId)));
        log.debug("Booking with id: {} is in repository", bookingId);

        if (booking.getStatus().equals(APPROVED) && approved) {
            throw new BookingUpdateStatusException(
                    String.format("Unable to approve the booking. Booking with id: %d has already been approved", bookingId));
        }

        validateOwnerAccess(userId, booking);

        booking.setStatus(approved ? APPROVED : REJECTED);
        log.debug("Status set to {}", booking.getStatus());

        return toBookingResponse(bookingRepository.save(booking));
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

    private List<BookingResponseDto> readBookings(Integer userId, String state, boolean isOwner) {
        log.trace("Searching for bookings (owner: {}) with id: {} has started (at service layer)", isOwner, userId);

        userService.getById(userId);
        log.debug("User with id: {} is in repository", userId);

        LocalDateTime now = LocalDateTime.now();
        log.trace("Current Date-Time set: {}", now);

        List<Booking> bookings = switch (state) {
            case "CURRENT" -> isOwner
                              ? bookingRepository.findBookingsByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now)
                              : bookingRepository.findBookingsByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
            case "PAST" -> isOwner
                           ? bookingRepository.findBookingsByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, now)
                           : bookingRepository.findBookingsByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
            case "FUTURE" -> isOwner
                             ? bookingRepository.findBookingsByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now)
                             : bookingRepository.findBookingsByBookerIdAndStartAfterOrderByStartDesc(userId, now);
            case "WAITING" -> isOwner
                              ? bookingRepository.findBookingsByItemOwnerIdAndStatusOrderByStartDesc(userId, WAITING)
                              : bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(userId, WAITING);
            case "REJECTED" -> isOwner
                               ? bookingRepository.findBookingsByItemOwnerIdAndStatusOrderByStartDesc(userId, REJECTED)
                               : bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(userId, REJECTED);
            default -> isOwner
                       ? bookingRepository.findBookingsByItemOwnerIdOrderByStartDesc(userId)
                       : bookingRepository.findBookingsByBookerIdOrderByStartDesc(userId);
        };
        log.debug("Booking status set");

        return bookings.stream().map(BookingDtoMapper::toBookingResponse).toList();
    }
}