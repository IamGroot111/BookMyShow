package com.BookingSystem.BookMyShow.repository;

import com.BookingSystem.BookMyShow.model.Slot;
import com.BookingSystem.BookMyShow.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByShow(Show show);
    Optional<Slot> findByShowAndTimeRange(Show show, String timeRange);
    List<Slot> findByShow_Name(String name);
}
