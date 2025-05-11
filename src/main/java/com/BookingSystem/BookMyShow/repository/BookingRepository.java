package com.BookingSystem.BookMyShow.repository;

import com.BookingSystem.BookMyShow.model.Booking;
import com.BookingSystem.BookMyShow.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findBySlot(Slot slot);
    List<Booking> findByUsername(String username);
}
