package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    boolean existsByEmailIgnoreCase(String email);
}
