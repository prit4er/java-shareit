package ru.practicum.shareit.comments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {

    @JsonProperty(access = READ_ONLY)
    Long id;
    @NotBlank(message = "Comment text is required")
    String text;
    String authorName;
    @JsonProperty(access = READ_ONLY)
    LocalDateTime created;
}