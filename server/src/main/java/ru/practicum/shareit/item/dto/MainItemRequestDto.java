package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainItemRequestDto {
    @JsonProperty(access = READ_ONLY)
    Long id;
    @NotBlank(message = "Name is required")
    String name;
    @NotBlank(message = "Description is required")
    String description;
    @NotNull(message = "Field 'available' is required")
    Boolean available;
    Long owner;
    @JsonProperty(access = READ_ONLY)
    ItemRequest request;
}