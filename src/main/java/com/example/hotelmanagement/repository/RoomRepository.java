package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByRoomNumberIgnoreCase(String roomNumber);
    List<Room> findByStatusOrderByRoomNumberAsc(String status);
}
