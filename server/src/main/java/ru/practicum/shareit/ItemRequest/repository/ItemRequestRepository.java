package ru.practicum.shareit.ItemRequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.ItemRequest.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestor_Id(Long id);

    Optional<ItemRequest> findAllByIdAndRequestor_Id(Long requestId, Long userId);
}
