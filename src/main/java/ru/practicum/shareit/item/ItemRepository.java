package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    ItemDto save(ItemDto item);

    Optional<ItemDto> findById(Integer id);

    List<ItemDto> findAll();

    void deleteById(Integer id);

    List<ItemDto> findByOwnerId(Integer ownerId);
}
