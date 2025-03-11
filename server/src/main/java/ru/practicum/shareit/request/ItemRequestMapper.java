package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.ItemDtoMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {
    public static ItemRequestDto toDto(ItemRequest request) {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setId(request.getRequestId());
        requestDto.setDescription(request.getDescription());
        requestDto.setCreated(request.getCreated());
        requestDto.setItems(request.getItems() != null ?
                            request.getItems().stream()
                                   .map(ItemDtoMapper::toDto)
                                   .toList() : Collections.emptyList());
        return requestDto;
    }

    public static ItemRequest toEntity(ItemRequestDto requestDto, User owner) {
        ItemRequest request = new ItemRequest();
        request.setDescription(requestDto.getDescription());
        request.setCreated(requestDto.getCreated() != null ? requestDto.getCreated() : LocalDateTime.now());
        request.setItems(requestDto.getItems() != null ?
                         requestDto.getItems().stream()
                                   .map(itemDto -> ItemDtoMapper.toEntity(itemDto, owner, request))
                                   .toList() : new ArrayList<>());
        return request;
    }

    public static void updateRequestFields(ItemRequest request, ItemRequestDto requestDto) {
        if (requestDto.getDescription() != null) {
            request.setDescription(requestDto.getDescription());
        }
        if (requestDto.getCreated() != null) {
            request.setCreated(requestDto.getCreated());
        }
    }
}
