package com.example.hotelmanagement.controller;

import com.example.hotelmanagement.repository.BookingRepository;
import com.example.hotelmanagement.repository.GuestRepository;
import com.example.hotelmanagement.repository.RoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final RoomRepository rooms;
    private final GuestRepository guests;
    private final BookingRepository bookings;
    public DashboardController(RoomRepository rooms, GuestRepository guests, BookingRepository bookings) {
        this.rooms = rooms; this.guests = guests; this.bookings = bookings;
    }
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("roomCount", rooms.count());
        model.addAttribute("guestCount", guests.count());
        model.addAttribute("bookingCount", bookings.count());
        model.addAttribute("confirmedCount", bookings.countByStatus("CONFIRMED"));
        model.addAttribute("recentBookings", bookings.findAllByOrderByCheckInDesc().stream().limit(5).toList());
        return "index";
    }
}
