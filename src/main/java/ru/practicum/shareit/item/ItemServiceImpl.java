package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingDtoMapper;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.comments.Comment;
import ru.practicum.shareit.comments.CommentDtoMapper;
import ru.practicum.shareit.comments.CommentRepository;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.exeptions.AccessDeniedException;
import ru.practicum.shareit.exeptions.NotFoundException;
import ru.practicum.shareit.exeptions.UserNotValidToCommentException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemDtoMapper.toEntityFromItemRequest;
import static ru.practicum.shareit.item.ItemDtoMapper.toItemRequestFromEntity;


@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingService bookingService;

    @Override
    public ItemDto create(Integer userId, ItemDto itemDto) {
        log.trace("Adding item at service level has started");

        User user = validateUserIsInRepository(userId);
        log.debug("Item owner exists. Start of adding owner to item");

        Item item = ItemDtoMapper.toEntity(itemDto, user);
        item.setOwner(user);
        log.trace("Owner is set. Item is ready to be added to repository");

        return ItemDtoMapper.toDto(itemRepository.save(item));
    }

    @Override
    public CommentDto createComment(Integer userId, Integer itemId, CommentDto commentDto) {
        log.trace("Adding comment by user with id: {} for item with id: {} is started (at service layer)",
                  userId, itemId);

        User author = validateUserIsInRepository(userId);
        log.debug("User-commentator exists");

        Item item = toEntityFromItemRequest(findById(userId, itemId));

        validateTheAbilityToComment(userId);
        log.debug("User has rights to comment this item");

        return CommentDtoMapper.toDto(commentRepository.save(CommentDtoMapper.toEntity(commentDto, item, author)));
    }

    @Override
    public ItemRequest findById(Integer userId, Integer id) {
        log.trace("Searching for item with id: {} has started (at service layer)", id);

        Item item = itemRepository.findById(id).orElseThrow(() ->
                                                                    new NotFoundException(
                                                                            String.format("Item with id = %d is not in repository", id)));
        log.debug("Item with id: {} is in repository. Start of adding bookings and comments...", id);

        ItemRequest itemRequest = toItemRequestFromEntity(item);

        setComments(itemRequest, item.getComments());
        log.debug("Comments were set for item with id: {}", id);

        if (Objects.equals(itemRequest.getOwner(), userId)) {
            setBookings(itemRequest, item.getBookings());
            log.debug("Bookings were set for item with id: {} (request from the owner of the thing with id: {})",
                      id, userId);
        }

        return itemRequest;
    }

    public Collection<ItemRequest> findForTheUser(Integer userId) {
        return itemRepository.findAllByOwnerIdInFull(userId).stream()
                             .map(item -> {
                                 ItemRequest itemRequest = toItemRequestFromEntity(item);
                                 if (item.getOwner().getId().equals(userId)) {
                                     setBookings(itemRequest, item.getBookings());
                                 }
                                 return itemRequest;
                             })
                             .toList();
    }

    @Override
    public ItemDto update(Integer userId, Integer id, ItemDto itemDto) {
        log.trace("Update of item with id: {} has started (at service layer)", id);

        validateUserIsInRepository(userId);
        log.debug("Item owner (id: {}) exists", userId);

        Item oldItemForUpdate = itemRepository.findById(id)
                                              .orElseThrow(() -> new NotFoundException("Item not found"));
        log.debug("Item with id: {} exists", id);

        if (!Objects.equals(oldItemForUpdate.getOwner().getId(), userId)) {
            log.error("User with id: {} is not owner of item with id {}", userId, id);
            throw new AccessDeniedException("User can't update someone else's item");
        }
        log.debug("The item can be updated. Start of update...");

        updateItemFields(oldItemForUpdate, itemDto);

        return ItemDtoMapper.toDto(itemRepository.save(oldItemForUpdate));
    }

    @Override
    public void deleteById(Integer id) {
        log.trace("Item, with id: {} deletion started (at service layer)", id);
        itemRepository.findById(id).orElseThrow(() ->
                                                        new NotFoundException(String.format("Item with id = %d is not in repository", id)));
        log.debug("Item with id: {} is in repository and can be deleted", id);
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> search(String text) {
        log.trace("Start of searching for items whose name or description contains text: {}", text);

        if (text.isBlank()) {
            log.debug("Text parameter is blank. Search is finished");
            return List.of();
        }

        return itemRepository.search(text).stream()
                             .map(ItemDtoMapper::toDto)  // Используем маппер
                             .collect(Collectors.toList());
    }

    private User validateUserIsInRepository(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                                                                   new NotFoundException(
                                                                           String.format("User with id: %d is not in repository", userId)));
    }

    private void validateTheAbilityToComment(Integer userId) {
        bookingService.getBookingsForUser(userId, "ALL").stream()
                      .filter(bookingResponse -> Objects.equals(bookingResponse.getBooker().getId(), userId) &&
                              bookingResponse.getEnd().isBefore(LocalDateTime.now()))
                      .findFirst().orElseThrow(() -> new UserNotValidToCommentException(
                              "The user can comment only if he/she has rented the item and the rental period has ended"));
    }

    private void updateItemFields(Item target, ItemDto source) {
        log.debug("Start of fields update");

        if (source.getName() != null) {
            target.setName(source.getName());
            log.debug("Item name was updated");
        }
        if (source.getDescription() != null) {
            target.setDescription(source.getDescription());
            log.debug("Item description was updated");
        }
        if (source.getAvailable() != null) {
            target.setAvailable(source.getAvailable());
            log.debug("Item available status was updated");
        }
        log.debug("Item fields update is finished");
    }

    private void setComments(ItemRequest itemRequest, Set<Comment> comments) {
        itemRequest.setComments(comments.stream()
                                        .map(CommentDtoMapper::toDto)
                                        .toList());
    }

    private void setBookings(ItemRequest itemRequest, Set<Booking> bookings) {
        LocalDateTime now = LocalDateTime.now();

        itemRequest.setLastBooking(
                bookings.stream()
                        .filter(booking -> booking.getStatus() != BookingStatus.REJECTED)
                        .filter(booking -> booking.getEnd().isBefore(now))
                        .max(Comparator.comparing(Booking::getEnd))
                        .map(BookingDtoMapper::toDto)
                        .orElse(null)
        );

        itemRequest.setNextBooking(
                bookings.stream()
                        .filter(booking -> booking.getStatus() != BookingStatus.REJECTED)
                        .filter(booking -> booking.getStart().isAfter(now))
                        .min(Comparator.comparing(Booking::getStart))
                        .map(BookingDtoMapper::toDto)
                        .orElse(null)
        );
    }
}
