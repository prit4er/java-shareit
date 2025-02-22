package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item save(Item item);

    Optional<Item> findById(Integer id);

    List<Item> findAll();

    void deleteById(Integer id);

    List<Item> findByOwnerId(Integer ownerId);
}
