package com.example.hotelmanagement.service;

import com.example.hotelmanagement.entity.Booking;
import com.example.hotelmanagement.entity.Guest;
import com.example.hotelmanagement.entity.Room;
import com.example.hotelmanagement.repository.BookingRepository;
import com.example.hotelmanagement.repository.GuestRepository;
import com.example.hotelmanagement.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class BookingService {
    private final BookingRepository bookings;
    private final GuestRepository guests;
    private final RoomRepository rooms;

    public BookingService(BookingRepository bookings, GuestRepository guests, RoomRepository rooms) {
        this.bookings = bookings; this.guests = guests; this.rooms = rooms;
    }

    @Transactional
    public Booking create(Long guestId, Long roomId, LocalDate checkIn, LocalDate checkOut, String notes) {
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past.");
        }
        Guest guest = guests.findById(guestId).orElseThrow(() -> new IllegalArgumentException("Please select a valid guest."));
        Room room = rooms.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Please select a valid room."));
        if (!"AVAILABLE".equalsIgnoreCase(room.getStatus())) {
            throw new IllegalArgumentException("This room is not available for booking.");
        }
        if (bookings.hasOverlappingBooking(roomId, checkIn, checkOut)) {
            throw new IllegalArgumentException("This room already has a booking during those dates.");
        }
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        Booking booking = new Booking();
        booking.setGuest(guest);
        booking.setRoom(room);
        booking.setCheckIn(checkIn);
        booking.setCheckOut(checkOut);
        booking.setTotalPrice(room.getPricePerNight().multiply(BigDecimal.valueOf(nights)));
        booking.setStatus("CONFIRMED");
        booking.setNotes(notes == null || notes.isBlank() ? null : notes.trim());
        return bookings.save(booking);
    }

    @Transactional
    public void cancel(Long id) {
        Booking booking = bookings.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found."));
        booking.setStatus("CANCELLED");
        bookings.save(booking);
    }
}
