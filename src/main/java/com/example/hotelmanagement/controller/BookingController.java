package com.example.hotelmanagement.controller;

import com.example.hotelmanagement.repository.BookingRepository;
import com.example.hotelmanagement.repository.GuestRepository;
import com.example.hotelmanagement.repository.RoomRepository;
import com.example.hotelmanagement.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    private final BookingRepository bookings;
    private final GuestRepository guests;
    private final RoomRepository rooms;
    private final BookingService bookingService;
    public BookingController(BookingRepository bookings, GuestRepository guests, RoomRepository rooms, BookingService bookingService) {
        this.bookings = bookings; this.guests = guests; this.rooms = rooms; this.bookingService = bookingService;
    }
    @GetMapping public String list(Model model) {
        model.addAttribute("bookings", bookings.findAllByOrderByCheckInDesc()); return "bookings/list";
    }
    @GetMapping("/new") public String form(Model model) {
        model.addAttribute("bookingForm", new BookingForm()); populateForm(model); return "bookings/form";
    }
    @PostMapping public String create(@Valid @ModelAttribute BookingForm bookingForm, BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) { populateForm(model); return "bookings/form"; }
        try {
            bookingService.create(bookingForm.getGuestId(), bookingForm.getRoomId(), bookingForm.getCheckIn(), bookingForm.getCheckOut(), bookingForm.getNotes());
            redirect.addFlashAttribute("success", "Booking created successfully."); return "redirect:/bookings";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage()); populateForm(model); return "bookings/form";
        }
    }
    @PostMapping("/{id}/cancel") public String cancel(@PathVariable Long id, RedirectAttributes redirect) {
        try { bookingService.cancel(id); redirect.addFlashAttribute("success", "Booking cancelled."); }
        catch (IllegalArgumentException ex) { redirect.addFlashAttribute("error", ex.getMessage()); }
        return "redirect:/bookings";
    }
    private void populateForm(Model model) {
        model.addAttribute("guests", guests.findAll());
        model.addAttribute("rooms", rooms.findByStatusOrderByRoomNumberAsc("AVAILABLE"));
    }
}
