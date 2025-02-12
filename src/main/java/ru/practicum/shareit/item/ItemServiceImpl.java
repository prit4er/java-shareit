package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeptions.ItemNotFoundException;
import ru.practicum.shareit.exeptions.UserAccessDeniedException;
import ru.practicum.shareit.exeptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
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
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }
        if (itemDto.getAvailable() == null) {
            throw new IllegalArgumentException("Available status must be provided");
        }
        if (itemDto.getName() == null || itemDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name must be provided and cannot be empty");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description must be provided and cannot be empty");
        }
        itemDto.setOwnerId(userId);
        return itemRepository.save(itemDto);
    }

    @Override
    public ItemDto updateItem(Integer userId, ItemDto itemDto) {
        Integer itemId = itemDto.getId();
        ItemDto existingItem = itemRepository.findById(itemId)
                                             .orElseThrow(() -> new ItemNotFoundException("Item not found"));
        if (!existingItem.getOwnerId().equals(userId)) {
            //Вот здесь общий эксепшен AccessDeniedException не работает =(
            throw new UserAccessDeniedException("User is not the owner");
        }
        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }
        return itemRepository.save(existingItem);
    }

    @Override
    public ItemDto getItem(Integer itemId) {
        return itemRepository.findById(itemId)
                             .orElseThrow(() -> new IllegalArgumentException("Item not found"));
    }

    @Override
    public List<ItemDto> getUserItems(Integer userId) {
        return itemRepository.findByOwnerId(userId);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String lowerCaseText = text.toLowerCase();
        return itemRepository.findAll().stream()
                             .filter(item -> Boolean.TRUE.equals(item.getAvailable()) &&
                                     ((item.getName() != null && item.getName().toLowerCase().contains(lowerCaseText)) ||
                                             (item.getDescription() != null && item.getDescription().toLowerCase().contains(
                                                     lowerCaseText))))
                             .collect(Collectors.toList());
    }
}
