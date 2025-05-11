package com.BookingSystem.BookMyShow.repository;

import com.BookingSystem.BookMyShow.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, String> {
    List<Show> findByGenre(String genre);
    // List<Show> findAll();
}
