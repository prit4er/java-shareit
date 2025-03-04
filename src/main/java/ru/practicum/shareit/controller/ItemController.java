package ru.practicum.shareit.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.models.comments.dto.CommentDto;
import ru.practicum.shareit.models.item.ItemService;
import ru.practicum.shareit.models.item.dto.ItemDto;
import ru.practicum.shareit.models.item.request.ItemRequest;

import java.util.Collection;
import java.util.List;

import static ru.practicum.shareit.constans.Constants.HEADER_USER_ID;

@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> addItem(
            @RequestHeader(HEADER_USER_ID) Integer userId,
            @Valid @RequestBody ItemDto itemDto) {
        ItemDto createdItem = itemService.create(userId, itemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader(HEADER_USER_ID) Integer userId,
                                              @PathVariable Integer itemId,
                                              @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        ItemDto updatedItem = itemService.update(userId, itemId, itemDto);
        return ResponseEntity.ok(updatedItem);
    }

    @GetMapping("/{itemId}")
    public ItemRequest read(@Positive @RequestHeader(HEADER_USER_ID) Integer userId,
                            @Positive @PathVariable Integer itemId) {
        return itemService.findById(userId, itemId);
    }

    @GetMapping
    public Collection<ItemRequest> readForTheUser(@Positive @RequestHeader(HEADER_USER_ID) Integer userId) {
        return itemService.findForTheUser(userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@Positive @PathVariable Integer id) {
        itemService.deleteById(id);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Positive @RequestHeader(HEADER_USER_ID) Integer userId,
                                    @Positive @PathVariable Integer itemId,
                                    @Valid @RequestBody CommentDto commentDto) {
        return itemService.createComment(userId, itemId, commentDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.search(text);
    }
}
