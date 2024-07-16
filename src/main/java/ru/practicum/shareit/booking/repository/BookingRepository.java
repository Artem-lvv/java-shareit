package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByIdAndItemOwnerId(Long bookingId, Long userId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.id = :bookingId AND (b.booker.id = :userId OR b.item.owner.id = :userId)" +
            "ORDER BY b.id")
    Optional<Booking> findBookingByBookingIdAndBookerIdOrItemOwnerId(@Param("bookingId") Long bookingId,
                                                                    @Param("userId") Long userId);

    List<Booking> findAllByBooker_Id(Long userId, Sort sort);
    @Query(value = "SELECT b " +
            "FROM Booking b " +
            "WHERE b.status = :status AND b.booker.id = :bookerId " +
            "ORDER BY b.id DESC")
    List<Booking> findBookingsByStatusAndBookerId(@Param("bookerId") Long userId,
                                                  @Param("status") BookingStatus status);

    @Query(value = "SELECT b " +
            "FROM Booking b " +
            "WHERE b.start <= CURRENT_TIMESTAMP AND b.end >= CURRENT_TIMESTAMP AND b.booker.id = :bookerId " +
            "ORDER BY b.id DESC")
    List<Booking> findCurrentBookings(@Param("bookerId") Long userId);

    @Query(value = "SELECT b " +
            "FROM Booking b " +
            "WHERE b.end < CURRENT_TIMESTAMP AND b.booker.id = :bookerId " +
            "ORDER BY b.id DESC")
    List<Booking> findPastBookings(@Param("bookerId") Long userId);

    @Query(value = "SELECT b " +
            "FROM Booking b " +
            "WHERE b.start > CURRENT_TIMESTAMP AND b.booker.id = :bookerId " +
            "ORDER BY b.id DESC")
    List<Booking> findFutureBookings(@Param("bookerId") Long userId);



    List<Booking> findAllByItemOwnerId(Long userId, Sort sort);

    @Query(value = "SELECT b " +
            "FROM Booking b " +
            "WHERE b.status = :status AND b.item.owner.id = :ownerId " +
            "ORDER BY b.id DESC")
    List<Booking> findBookingsByStatusAndBookerIdForOwner(@Param("ownerId") Long userId,
                                                          @Param("status") BookingStatus status);

    @Query(value = "SELECT b " +
            "FROM Booking b " +
            "WHERE b.start <= CURRENT_TIMESTAMP AND b.end >= CURRENT_TIMESTAMP AND b.item.owner.id = :ownerId " +
            "ORDER BY b.id DESC")
    List<Booking> findCurrentBookingsForOwner(@Param("ownerId") Long userId);

    @Query(value = "SELECT b " +
            "FROM Booking b " +
            "WHERE b.end < CURRENT_TIMESTAMP AND b.item.owner.id = :ownerId " +
            "ORDER BY b.id DESC")
    List<Booking> findPastBookingsForOwner(@Param("ownerId") Long userId);

    @Query(value = "SELECT b " +
            "FROM Booking b " +
            "WHERE b.start > CURRENT_TIMESTAMP AND b.item.owner.id = :ownerId " +
            "ORDER BY b.id DESC")
    List<Booking> findFutureBookingsForOwner(@Param("ownerId") Long userId);
}
