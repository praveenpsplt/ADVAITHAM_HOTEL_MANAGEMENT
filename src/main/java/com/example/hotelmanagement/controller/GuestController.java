package com.example.hotelmanagement.controller;

import com.example.hotelmanagement.entity.Guest;
import com.example.hotelmanagement.repository.GuestRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guests")
public class GuestController {
    private final GuestRepository guests;
    public GuestController(GuestRepository guests) { this.guests = guests; }

    @GetMapping public String list(Model model) { model.addAttribute("guests", guests.findAll()); return "guests/list"; }
    @GetMapping("/new") public String createForm(Model model) { model.addAttribute("guest", new Guest()); return "guests/form"; }
    @PostMapping public String create(@Valid @ModelAttribute Guest guest, BindingResult result, RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) return "guests/form";
        try {
            guest.setEmail(guest.getEmail().trim().toLowerCase()); guests.save(guest);
            redirect.addFlashAttribute("success", "Guest registered successfully."); return "redirect:/guests";
        } catch (DataIntegrityViolationException ex) {
            model.addAttribute("error", "A guest with this email already exists."); return "guests/form";
        }
    }
}
