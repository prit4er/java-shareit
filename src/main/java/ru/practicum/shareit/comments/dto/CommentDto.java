package ru.practicum.shareit.comments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CommentDto {

    Integer id;

    @NotBlank(message = "Not able to add comment with blank text")
    @Size(max = 255, message = "Maximum comment-text size exceeded (greater than 255)")
    String text;

    Integer itemId;

    String authorName;

    LocalDateTime created;
}
