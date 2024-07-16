package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "SELECT i FROM Item i WHERE i.owner.id = :ownerId")
    List<Item> findAllItemsByUserId(@Param("ownerId") Long userId);

    @Query(value = "SELECT * " +
            "FROM Items i " +
            "WHERE i.available = true AND (i.name ILIKE :searchText OR i.description ILIKE :searchText)",
            nativeQuery = true)
    List<Item> findItemsByText(@Param("searchText") String text);

}
