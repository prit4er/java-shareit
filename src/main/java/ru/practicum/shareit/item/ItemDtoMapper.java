package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ItemDtoMapper {

    public static ItemDto toDto(Item item) {
        if (item == null) {
            return null;
        }
        log.debug("Mapping Item with ID {} to ItemDto", item.getId());
        return ItemDto.builder()
                      .id(item.getId())
                      .name(item.getName())
                      .description(item.getDescription())
                      .available(item.getAvailable())
                      .owner(item.getOwner().getId())
                      .build();
    }

    public static Item toEntity(ItemDto itemDto, User owner) {
        if (itemDto == null) {
            return null;
        }
        return Item.builder()
                   .id(itemDto.getId())
                   .name(itemDto.getName())
                   .description(itemDto.getDescription())
                   .available(itemDto.getAvailable())
                   .owner(owner)
                   .build();
    }

    public static Item toEntityFromItemRequest(ItemRequest itemRequest) {
        return Item.builder()
                   .id(itemRequest.getId())
                   .name(itemRequest.getName())
                   .description(itemRequest.getDescription())
                   .available(itemRequest.getAvailable())
                   .build();

    }

    public static ItemRequest toItemRequestFromEntity(Item item) {
        return ItemRequest.builder()
                          .id(item.getId())
                          .name(item.getName())
                          .description(item.getDescription())
                          .available(item.getAvailable())
                          .owner(item.getOwner().getId())
                          .build();
    }

    public static List<ItemDto> toDto(List<Item> items) {
        return items == null ? List.of() : items.stream()
                                                .map(ItemDtoMapper::toDto)
                                                .collect(Collectors.toList());
    }

    public static List<Item> toEntity(List<ItemDto> itemDtos, User owner) {
        return itemDtos == null ? List.of() : itemDtos.stream()
                                                      .map(dto -> toEntity(dto, owner))
                                                      .collect(Collectors.toList());
    }
}