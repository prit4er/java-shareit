package ru.practicum.shareit.comments;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentDtoMapper {

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static Comment toEntity(CommentDto commentDto, Item item, User author) {
        if (commentDto == null || item == null || author == null) {
            throw new IllegalArgumentException("CommentDto, Item, and User must not be null");
        }
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                item,
                author,
                commentDto.getCreated() != null ? commentDto.getCreated() : LocalDateTime.now()
        );
    }
}