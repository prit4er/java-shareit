package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comments.CommentDtoMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MainItemRequestDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemDtoMapper {

    public static ItemDto toDto(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        return ItemDto.builder()
                      .id(item.getItemId())
                      .name(item.getName())
                      .description(item.getDescription())
                      .available(item.isAvailable())
                      .owner(item.getOwnerId())
                      .requestId(item.getRequest() != null ? item.getRequest().getRequestId() : null)
                      .comments(item.getComments() != null
                                ? item.getComments().stream().map(CommentDtoMapper::toDto).toList()
                                : Collections.emptyList())
                      .build();
    }

    public static Item toEntity(ItemDto itemDto, User owner, ItemRequest request) {
        if (itemDto == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        return Item.builder()
                   .name(itemDto.getName())
                   .description(itemDto.getDescription())
                   .isAvailable(itemDto.getAvailable())
                   .ownerId(owner.getUserId())
                   .request(request)
                   .comments(itemDto.getComments() != null
                             ? itemDto.getComments().stream()
                                      .map(commentDto -> CommentDtoMapper.toEntity(commentDto, null, owner))
                                      .toList()
                             : new ArrayList<>())
                   .build();
    }

    public static void updateItemFields(Item item, MainItemRequestDto itemUpdateDto) {
        Optional.ofNullable(itemUpdateDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemUpdateDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemUpdateDto.getAvailable()).ifPresent(item::setAvailable);
        Optional.ofNullable(itemUpdateDto.getRequest()).ifPresent(item::setRequest);
    }
}