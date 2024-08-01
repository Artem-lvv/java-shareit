package ru.practicum.shareit.ItemRequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.ItemRequest.model.ItemRequest;
import ru.practicum.shareit.ItemRequest.model.ItemRequestWithItems;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestor_Id(Long id);

    Optional<ItemRequest> findAllByIdAndRequestor_Id(Long requestId, Long userId);
}
