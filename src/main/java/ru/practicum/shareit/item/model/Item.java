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

    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private Integer ownerId;
}
