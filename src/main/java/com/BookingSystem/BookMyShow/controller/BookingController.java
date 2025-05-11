package com.BookingSystem.BookMyShow.controller;

import com.BookingSystem.BookMyShow.model.Booking;
import com.BookingSystem.BookMyShow.model.Show;
import com.BookingSystem.BookMyShow.model.Slot;
import com.BookingSystem.BookMyShow.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Register a new show
    @PostMapping("/registerShow")
    public String registerShow(@RequestParam String showName, @RequestParam String genre) {
        bookingService.registerShow(showName, genre);
        return "Show " + showName + " registered successfully!";
    }

    // Onboard a show slot
    @PostMapping("/onboardShowSlots")
    public String onboardShowSlots(@RequestParam String showName, @RequestParam String timeSlot, @RequestParam int seatCount) {
        bookingService.onboardShowSlots(showName, timeSlot, seatCount);
        return "Slots for show " + showName + " onboarded successfully!";
    }

    // Book a ticket
    @PostMapping("/bookTicket")
    public String bookTicket(@RequestParam String username, @RequestParam String showName, @RequestParam String timeSlot, @RequestParam int seatCount) {
        String bookingId = bookingService.bookTicket(username, showName, timeSlot, seatCount);
        return "Booking successful with ID: " + bookingId;
    }

    // Get bookings for a particular show
    @GetMapping("/getBookingsForShow")
    public List<Booking> getBookingsForShow(@RequestParam String showName) {
        return bookingService.getBookingsForShow(showName);
    }

    // Cancel a booking
    @DeleteMapping("/cancelBooking")
    public String cancelBooking(@RequestParam String bookingId) {
        bookingService.cancelBooking(bookingId);
        return "Booking " + bookingId + " cancelled successfully!";
    }

    @GetMapping("/availableShowsByGenre")
    public List<String> getAvailableShowsByGenre(@RequestParam String genre) {
        return bookingService.showAvailByGenre(genre);
    }
    
    // Get trending show
    @GetMapping("/trendingShow")
    public ResponseEntity<String> getTrendingShow() {
    return bookingService.showTrendingShow()
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No trending show found"));
    }

    // Get all shows
    @GetMapping("/getAllShows")
    public List<Show> getAllShows() {
        return bookingService.getAllShows();
    }

    // Get all slots for a show
    @GetMapping("/getSlotsForShow")
    public List<Slot> getSlotsForShow(@RequestParam String showName) {
        return bookingService.getSlotsForShow(showName);
    }

    // Clear all data
    @GetMapping("/clear")
    public void clearData() {
        bookingService.clearData();
    }
}
