package ru.practicum.shareit.ItemRequest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.item.Item;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ItemRequestWithItems {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<Item> items;
}
