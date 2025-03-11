package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.ItemDtoMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    public static ItemRequestDto toDto(ItemRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        return ItemRequestDto.builder()
                             .id(request.getRequestId())
                             .description(request.getDescription())
                             .created(request.getCreated())
                             .items(request.getItems() != null
                                    ? request.getItems().stream().map(ItemDtoMapper::toDto).toList()
                                    : Collections.emptyList())
                             .build();
    }

    public static ItemRequest toEntity(ItemRequestDto requestDto, User owner) {
        if (requestDto == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        return ItemRequest.builder()
                          .description(requestDto.getDescription())
                          .created(requestDto.getCreated() != null ? requestDto.getCreated() : LocalDateTime.now())
                          .items(requestDto.getItems() != null
                                 ? requestDto.getItems().stream()
                                             .map(itemDto -> ItemDtoMapper.toEntity(itemDto, owner, null))
                                             .toList()
                                 : new ArrayList<>())
                          .build();
    }

    public static void updateRequestFields(ItemRequest request, ItemRequestDto requestDto) {
        Optional.ofNullable(requestDto.getDescription()).ifPresent(request::setDescription);
        Optional.ofNullable(requestDto.getCreated()).ifPresent(request::setCreated);
    }
}
