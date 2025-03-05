package ru.practicum.shareit.item;

import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.item.request.ItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;

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