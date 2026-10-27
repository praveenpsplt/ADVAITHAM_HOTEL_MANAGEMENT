package com.example.hotelmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(name = "room_number", nullable = false, unique = true, length = 20)
    private String roomNumber;

    @NotBlank @Column(nullable = false, length = 40)
    private String roomType;

    @Min(1) @Column(nullable = false)
    private int capacity;

    @DecimalMin("0.01") @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(nullable = false, length = 20)
    private String status = "AVAILABLE";

    public Room() {}
    public Room(String roomNumber, String roomType, int capacity, BigDecimal pricePerNight, String status) {
        this.roomNumber = roomNumber; this.roomType = roomType; this.capacity = capacity;
        this.pricePerNight = pricePerNight; this.status = status;
    }
    public Long getId() { return id; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
