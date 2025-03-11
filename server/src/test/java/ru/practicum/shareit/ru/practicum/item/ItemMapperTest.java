package ru.practicum.shareit.ru.practicum.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDtoMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MainItemRequestDto;
import ru.practicum.shareit.user.User;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemMapperTest {

    @Test
    void convertToDto() {
        Item item = new Item();
        item.setItemId(1L);
        item.setName("Hammer");
        item.setDescription("A hammer");
        item.setAvailable(true);
        item.setOwnerId(1L);
        item.setComments(Collections.emptyList());

        ItemDto result = ItemDtoMapper.toDto(item);

        assertEquals(1L, result.getId());
        assertEquals("Hammer", result.getName());
        assertEquals("A hammer", result.getDescription());
        assertTrue(result.getAvailable());
        assertEquals(1L, result.getOwner());
        assertTrue(result.getComments().isEmpty());
    }

    @Test
    void convertToEntity() {
        User owner = new User();
        owner.setUserId(1L);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Hammer");
        itemDto.setDescription("A hammer");
        itemDto.setAvailable(true);

        Item result = ItemDtoMapper.toEntity(itemDto, owner, null);

        assertEquals("Hammer", result.getName());
        assertEquals("A hammer", result.getDescription());
        assertTrue(result.isAvailable());
        assertEquals(1L, result.getOwnerId());
        assertNull(result.getRequest());
    }

    @Test
    void updateItemFields() {
        Item item = new Item();
        item.setItemId(1L);
        item.setName("Old Hammer");
        item.setDescription("Old desc");
        item.setAvailable(false);

        MainItemRequestDto mainItemRequestDto = new MainItemRequestDto();
        mainItemRequestDto.setName("New Hammer");
        mainItemRequestDto.setDescription("New desc");
        mainItemRequestDto.setAvailable(true);

        ItemDtoMapper.updateItemFields(item, mainItemRequestDto);

        assertEquals("New Hammer", item.getName());
        assertEquals("New desc", item.getDescription());
        assertTrue(item.isAvailable());
    }

    @Test
    void updateItemProvidedFields() {
        Item item = new Item();
        item.setItemId(1L);
        item.setName("Old Hammer");
        item.setDescription("Old desc");
        item.setAvailable(false);

        MainItemRequestDto updateDto = new MainItemRequestDto();
        updateDto.setName("New Hammer");

        ItemDtoMapper.updateItemFields(item, updateDto);

        assertEquals("New Hammer", item.getName());
        assertEquals("Old desc", item.getDescription());
        assertFalse(item.isAvailable());
    }
}