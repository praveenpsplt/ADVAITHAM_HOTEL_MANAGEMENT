package com.example.hotelmanagement;

import com.example.hotelmanagement.entity.Booking;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingEntityTest {
    @Test
    void calculatesNumberOfNights() {
        Booking booking = new Booking();
        booking.setCheckIn(LocalDate.of(2026, 11, 10));
        booking.setCheckOut(LocalDate.of(2026, 11, 13));
        assertEquals(3, booking.getNights());
    }
}
