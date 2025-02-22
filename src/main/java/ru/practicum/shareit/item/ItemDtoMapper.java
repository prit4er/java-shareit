package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemDtoMapper {

    // Маппинг Item -> ItemDto
    public static ItemDto toDto(Item item) {
        if (item == null) {
            return null;
        }
        return ItemDto.builder()
                      .id(item.getId())
                      .name(item.getName())
                      .description(item.getDescription())
                      .available(item.getAvailable())
                      .ownerId(item.getOwnerId())
                      .build();
    }

    // Маппинг ItemDto -> Item
    public static Item toEntity(ItemDto itemDto) {
        if (itemDto == null) {
            return null;
        }
        return Item.builder()
                   .id(itemDto.getId())
                   .name(itemDto.getName())
                   .description(itemDto.getDescription())
                   .available(itemDto.getAvailable())
                   .ownerId(itemDto.getOwnerId())
                   .build();
    }

    // Маппинг коллекции Item -> List<ItemDto>
    public static List<ItemDto> toDto(List<Item> items) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }
        return items.stream()
                    .map(ItemDtoMapper::toDto)
                    .collect(Collectors.toList());
    }

    // Маппинг коллекции ItemDto -> List<Item>
    public static List<Item> toEntity(List<ItemDto> itemDtos) {
        if (itemDtos == null || itemDtos.isEmpty()) {
            return List.of();
        }
        return itemDtos.stream()
                       .map(ItemDtoMapper::toEntity)
                       .collect(Collectors.toList());
    }
}