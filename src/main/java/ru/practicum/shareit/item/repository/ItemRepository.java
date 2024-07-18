package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.model.item.ItemWithRelatedEntities;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT new ru.practicum.shareit.item.model.item.ItemWithRelatedEntities(i, lb, nb)\n" +
            "FROM Item i \n" +
            "       LEFT JOIN FETCH Booking lb ON lb.item.id = i.id AND lb.end = (SELECT MAX(b.end)\n" +
            "                                                               FROM Booking b\n" +
            "                                                               WHERE b.item.id = i.id\n" +
            "                                                                 AND b.end <= CURRENT_TIMESTAMP\n" +
            "                                                                 AND b.status = 'APPROVED'\n" +
            "                                                                 AND b.item.owner.id = :ownerId)\n" +
            "       LEFT JOIN FETCH Booking nb ON nb.item.id = i.id AND nb.start = (SELECT MIN(b.start)\n" +
            "                                                                 FROM Booking b\n" +
            "                                                                 WHERE b.item.id = i.id\n" +
            "                                                                   AND b.start >= CURRENT_TIMESTAMP\n" +
            "                                                                   AND b.status = 'APPROVED'\n" +
            "                                                                   AND b.item.owner.id = :ownerId)\n" +
            "WHERE i.owner.id = :ownerId")
    List<ItemWithRelatedEntities> findAllItemsByOwnerId(@Param("ownerId") Long userId);

    @Query(value = "SELECT * " +
            "FROM Items i " +
            "WHERE i.available = true AND (i.name ILIKE :searchText OR i.description ILIKE :searchText)",
            nativeQuery = true)
    List<Item> findAllItemsByText(@Param("searchText") String text);

    @Query("SELECT new ru.practicum.shareit.item.model.item.ItemWithRelatedEntities(i, lb, nb)\n" +
            "FROM Item i \n" +
            "       LEFT JOIN FETCH Booking lb ON lb.item.id = i.id AND lb.end = (SELECT MAX(b.end)\n" +
            "                                                               FROM Booking b\n" +
            "                                                               WHERE b.item.id = i.id\n" +
            "                                                                 AND (b.start <= CURRENT_TIMESTAMP)\n" +
            "                                                                 AND b.status = 'APPROVED'\n" +
            "                                                                 AND b.item.owner.id = :itemOwnerId)\n" +
            "       LEFT JOIN FETCH Booking nb ON nb.item.id = i.id AND nb.start = (SELECT MIN(b.start)\n" +
            "                                                                 FROM Booking b\n" +
            "                                                                 WHERE b.item.id = i.id\n" +
            "                                                                   AND b.start >= CURRENT_TIMESTAMP\n" +
            "                                                                   AND b.status = 'APPROVED'\n" +
            "                                                                   AND b.item.owner.id = :itemOwnerId)\n" +
            "WHERE i.id = :itemId")
    Optional<ItemWithRelatedEntities> findItemById(@Param("itemId") Long itemId,
                                                   @Param("itemOwnerId") Long userId);

}
