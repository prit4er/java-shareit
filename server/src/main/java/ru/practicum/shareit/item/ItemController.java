package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MainItemRequestDto;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    public static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> getItems(@RequestHeader(HEADER_USER_ID) long userId) {
        log.info("Received GET request for all items for user with id: {}", userId);
        return itemService.findByUserId(userId);
    }

    @GetMapping("/{item-id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItem(@PathVariable("item-id") long itemId) {
        log.info("Received GET request for item with id: {}", itemId);
        return itemService.getItemById(itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@RequestHeader(HEADER_USER_ID) long userId,
                           @RequestBody ItemDto itemDto) {
        log.info("Received POST request for item for user with id: {}", userId);
        return itemService.addItem(itemDto, userId);
    }

    @PatchMapping("/{item-id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestHeader(HEADER_USER_ID) long userId,
                              @PathVariable("item-id") long itemId,
                              @RequestBody MainItemRequestDto itemDto) {
        log.info("Received PATCH request for item with id: {} for user with id: {}", itemId, userId);
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @DeleteMapping("/{item-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@RequestHeader(HEADER_USER_ID) long userId,
                           @PathVariable("item-id") long itemId) {
        log.info("Received DELETE request for item with id: {} for user with id: {}", itemId, userId);
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> searchItems(@RequestParam("text") String text) {
        log.info("Received GET request for items with text: {}", text);
        return itemService.searchItems(text);
    }

    @PostMapping("/{item-id}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto addComment(@RequestHeader(HEADER_USER_ID) Long userId,
                                 @PathVariable("item-id") Long itemId,
                                 @RequestBody CommentDto commentDto) {
        log.info("Received POST request for comment to item with id: {} by user with id: {}", itemId, userId);
        return itemService.addComment(itemId, userId, commentDto);
    }
}