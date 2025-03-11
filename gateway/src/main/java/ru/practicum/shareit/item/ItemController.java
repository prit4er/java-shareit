package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;


@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    public static final String USER_HEADER = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(USER_HEADER) @Positive long userId) {
        log.info("Sending GET request for all items for user with id: {}", userId);
        return itemClient.findByUserId(userId);
    }

    @GetMapping("/{item-id}")
    public ResponseEntity<Object> getItem(@PathVariable("item-id") @Positive long itemId) {
        log.info("Sending GET request for item with id: {}", itemId);
        return itemClient.getItemById(itemId);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(USER_HEADER) @Positive long userId,
                                          @RequestBody @Valid ItemDto itemDto) {
        log.info("Sending POST request for item {} for userId {}", itemDto, userId);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{item-id}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_HEADER) @Positive long userId,
                                             @PathVariable("item-id") @Positive long itemId, @RequestBody @Valid ItemUpdateDto itemDto) {
        log.info("Sending PATCH request for item with id: {} for user with id: {}", itemId, userId);
        return itemClient.updateItem(itemId, userId, itemDto);
    }

    @DeleteMapping("/{item-id}")
    public ResponseEntity<Object> deleteItem(@RequestHeader(USER_HEADER) @Positive long userId,
                                             @PathVariable("item-id") @Positive long itemId) {
        log.info("Sending DELETE request for item with id: {} for user with id: {}", itemId, userId);
        return itemClient.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam("text") @NotNull String text) {
        log.info("Sending GET request for items with text: {}", text);
        return itemClient.searchItems(text);
    }

    @PostMapping("/{item-id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(USER_HEADER) @Positive Long userId,
                                             @PathVariable("item-id") @Positive Long itemId,
                                             @RequestBody @Valid CommentDto commentDto) {
        log.info("Sending POST request for comment to item with id: {} by user with id: {}", itemId, userId);
        return itemClient.addComment(itemId, userId, commentDto);
    }
}
