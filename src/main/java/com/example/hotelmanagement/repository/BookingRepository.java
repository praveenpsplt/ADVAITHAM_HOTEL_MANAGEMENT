package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b join fetch b.guest join fetch b.room order by b.checkIn desc")
    List<Booking> findAllByOrderByCheckInDesc();
    long countByStatus(String status);

    @Query("select count(b) > 0 from Booking b where b.room.id = :roomId and b.status <> 'CANCELLED' " +
           "and b.checkIn < :checkOut and b.checkOut > :checkIn")
    boolean hasOverlappingBooking(@Param("roomId") Long roomId,
                                  @Param("checkIn") LocalDate checkIn,
                                  @Param("checkOut") LocalDate checkOut);
}
