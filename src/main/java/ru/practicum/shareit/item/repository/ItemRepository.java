package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemRepository {
    private Long currentMaxId = 0L;
    private final Map<Long, Item> idToItem = new HashMap<>();

    public Item save(Item item) {
        Long id = getNextId();

        item.setId(id);
        idToItem.put(id, item);

        return item;
    }

    public Item update(Item item) {
        idToItem.put(item.getId(), item);
        return item;
    }

    public void deleteById(Long itemId) {
        idToItem.remove(itemId);
    }

    public Optional<Item> findById(Long itemId) {
        return Optional.ofNullable(idToItem.get(itemId));
    }


    public List<Item> findAllItemsByUserId(Long userId) {
        return idToItem.values()
                .stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .toList();
    }

    private long getNextId() {
        return ++currentMaxId;
    }

    public List<Item> findItemsByText(String text) {
        return idToItem.values()
                .stream()
                .filter(item -> item.isAvailable() && (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .toList();
    }
}
