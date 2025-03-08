package ru.practicum.shareit.comments;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class CommentDtoMapper {

    public static CommentDto toDto(Comment comment) {
        if (Objects.isNull(comment)) {
            return null;
        }
        return CommentDto.builder()
                         .id(comment.getId())
                         .text(comment.getText())
                         .itemId(comment.getItem().getId())
                         .authorName(comment.getAuthor().getName())
                         .created(comment.getCreated())
                         .build();
    }

    public static Comment toEntity(CommentDto commentDto, Item item, User author) {
        return Comment.builder()
                      .text(commentDto.getText())
                      .item(item)
                      .author(author)
                      .created(LocalDateTime.now())
                      .build();
    }
}
