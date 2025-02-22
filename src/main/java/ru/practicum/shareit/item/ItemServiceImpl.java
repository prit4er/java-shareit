package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeptions.AccessDeniedException;
import ru.practicum.shareit.exeptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(Integer userId, ItemDto itemDto) {
        validateUserExists(userId);
        validateItemDto(itemDto);

        Item item = ItemDtoMapper.toEntity(itemDto);
        item.setOwnerId(userId);
        return ItemDtoMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Integer userId, ItemDto itemDto) {
        validateUserExists(userId);

        Item existingItem = itemRepository.findById(itemDto.getId())
                                          .orElseThrow(() -> new NotFoundException("Item not found"));

        if (!existingItem.getOwnerId().equals(userId)) {
            throw new AccessDeniedException("User is not the owner");
        }

        if (itemDto.getName() != null && !itemDto.getName().trim().isEmpty()) {
            existingItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().trim().isEmpty()) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }

        return ItemDtoMapper.toDto(itemRepository.save(existingItem));
    }

    @Override
    public ItemDto getItem(Integer itemId) {
        return itemRepository.findById(itemId)
                             .map(ItemDtoMapper::toDto)
                             .orElseThrow(() -> new NotFoundException("Item not found"));
    }

    @Override
    public List<ItemDto> getUserItems(Integer userId) {
        validateUserExists(userId);
        return itemRepository.findByOwnerId(userId).stream()
                             .map(ItemDtoMapper::toDto)
                             .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String lowerCaseText = text.toLowerCase();
        return itemRepository.findAll().stream()
                             .filter(item -> Boolean.TRUE.equals(item.getAvailable()))
                             .filter(item -> containsText(item, lowerCaseText))
                             .map(ItemDtoMapper::toDto)
                             .collect(Collectors.toList());
    }

    private boolean containsText(Item item, String text) {
        return (item.getName() != null && item.getName().toLowerCase().contains(text)) ||
                (item.getDescription() != null && item.getDescription().toLowerCase().contains(text));
    }

    private void validateUserExists(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
    }

    private void validateItemDto(ItemDto itemDto) {
        if (itemDto.getAvailable() == null) {
            throw new IllegalArgumentException("Available status must be provided");
        }
        if (itemDto.getName() == null || itemDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name must be provided and cannot be empty");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description must be provided and cannot be empty");
        }
    }
}