package ru.practicum.shareit.models.item;

import ru.practicum.shareit.models.comments.dto.CommentDto;
import ru.practicum.shareit.models.item.dto.ItemDto;
import ru.practicum.shareit.models.item.request.ItemRequest;

import java.util.Collection;
import java.util.List;

public interface ItemService {

    ItemDto create(Integer userId, ItemDto itemDto);

    CommentDto createComment(Integer userId, Integer itemId, CommentDto commentDto);

    ItemRequest findById(Integer userId, Integer id);

    Collection<ItemRequest> findForTheUser(Integer userId);

    ItemDto update(Integer userId, Integer id, ItemDto itemDto);

    void deleteById(Integer id);

    List<ItemDto> search(String text);
}