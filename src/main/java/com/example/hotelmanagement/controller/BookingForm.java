package com.example.hotelmanagement.controller;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class BookingForm {
    @NotNull private Long guestId;
    @NotNull private Long roomId;
    @NotNull private LocalDate checkIn;
    @NotNull private LocalDate checkOut;
    private String notes;
    public Long getGuestId() { return guestId; }
    public void setGuestId(Long guestId) { this.guestId = guestId; }
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
