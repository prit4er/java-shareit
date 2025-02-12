package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Item {

    private final Integer id;
    private final String name;
    private final String description;
    private final boolean available;
    private final Integer ownerId;
}
