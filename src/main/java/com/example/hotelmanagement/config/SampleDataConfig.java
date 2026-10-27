package com.example.hotelmanagement.config;

import com.example.hotelmanagement.entity.Guest;
import com.example.hotelmanagement.entity.Room;
import com.example.hotelmanagement.repository.GuestRepository;
import com.example.hotelmanagement.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;

@Configuration
public class SampleDataConfig {
    @Bean
    CommandLineRunner loadSampleData(RoomRepository rooms, GuestRepository guests) {
        return args -> {
            if (rooms.count() == 0) {
                rooms.save(new Room("101", "STANDARD", 2, new BigDecimal("2500.00"), "AVAILABLE"));
                rooms.save(new Room("102", "DELUXE", 2, new BigDecimal("3500.00"), "AVAILABLE"));
                rooms.save(new Room("201", "SUITE", 4, new BigDecimal("6500.00"), "AVAILABLE"));
            }
            if (guests.count() == 0) {
                Guest guest = new Guest(); guest.setFirstName("Demo"); guest.setLastName("Guest");
                guest.setEmail("demo.guest@example.com"); guest.setPhone("9000000000"); guests.save(guest);
            }
        };
    }
}
