package com.example.hotelmanagement.controller;

import com.example.hotelmanagement.entity.Room;
import com.example.hotelmanagement.repository.RoomRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rooms")
public class RoomController {
    private final RoomRepository rooms;
    public RoomController(RoomRepository rooms) { this.rooms = rooms; }

    @GetMapping public String list(Model model) {
        model.addAttribute("rooms", rooms.findAll().stream().sorted((a,b) -> a.getRoomNumber().compareToIgnoreCase(b.getRoomNumber())).toList());
        return "rooms/list";
    }
    @GetMapping("/new") public String createForm(Model model) {
        model.addAttribute("room", new Room()); return "rooms/form";
    }
    @PostMapping public String create(@Valid @ModelAttribute Room room, BindingResult result, RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) return "rooms/form";
        try {
            room.setStatus("AVAILABLE"); rooms.save(room);
            redirect.addFlashAttribute("success", "Room added successfully."); return "redirect:/rooms";
        } catch (DataIntegrityViolationException ex) {
            model.addAttribute("error", "Room number already exists."); return "rooms/form";
        }
    }
}
