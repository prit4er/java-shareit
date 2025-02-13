package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Integer, Item> items = new HashMap<>();
    private Integer idCounter = 0;

    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(++idCounter);
        }
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> findById(Integer id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public void deleteById(Integer id) {
        items.remove(id);
    }

    @Override
    public List<Item> findByOwnerId(Integer ownerId) {
        return items.values().stream()
                    .filter(item -> item.getOwnerId().equals(ownerId))
                    .collect(Collectors.toList());
    }
}