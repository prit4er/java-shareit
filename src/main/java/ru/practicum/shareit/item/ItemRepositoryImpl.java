package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Integer, ItemDto> items = new HashMap<>();
    private Integer idCounter = 0;

    @Override
    public ItemDto save(ItemDto item) {
        if (item.getId() == null) {
            item.setId(++idCounter);
        }
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<ItemDto> findById(Integer id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<ItemDto> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public void deleteById(Integer id) {
        items.remove(id);
    }

    @Override
    public List<ItemDto> findByOwnerId(Integer ownerId) {
        return items.values().stream()
                    .filter(item -> item.getOwnerId().equals(ownerId))
                    .collect(Collectors.toList());
    }
}
