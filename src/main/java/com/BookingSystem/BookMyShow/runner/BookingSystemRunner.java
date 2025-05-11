package com.BookingSystem.BookMyShow.runner;

import com.BookingSystem.BookMyShow.service.BookingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BookingSystemRunner implements CommandLineRunner {

    private final BookingService bookingService;

    public BookingSystemRunner(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting the Booking System...");

        // Register a new show
        bookingService.registerShow("TMKOC", "Comedy");
        bookingService.registerShow("BBT", "Comedy");

        // Onboard slots for the shows
        bookingService.onboardShowSlots("TMKOC", "9:00-10:00", 3);
        bookingService.onboardShowSlots("TMKOC", "10:00-11:00", 3);
        bookingService.onboardShowSlots("BBT", "18:00-19:00", 2);

        // Book tickets for users
        String bookingId1 = bookingService.bookTicket("User1", "TMKOC", "9:00-10:00", 2);
        String bookingId2 = bookingService.bookTicket("User2", "TMKOC", "9:00-10:00", 1);
        String bookingId3 = bookingService.bookTicket("User3", "BBT", "18:00-19:00", 2); // Waitlisted

        // Display the bookings for the show
        System.out.println("Bookings for TMKOC show:");
        bookingService.getBookingsForShow("TMKOC").forEach(booking ->
            System.out.println("Booking ID: " + booking.getBookingId() + ", User: " + booking.getUsername()));

        // Cancel a booking and check waitlist
        System.out.println("\nCancelling booking for User1...");
        bookingService.cancelBooking(bookingId1);

        // Check waitlisted bookings after cancellation
        System.out.println("\nWaitlist after cancellation:");
        bookingService.getBookingsForShow("BBT").forEach(booking ->
            System.out.println("Booking ID: " + booking.getBookingId() + ", User: " + booking.getUsername()));

        System.out.println("Booking system started successfully!");
    }
}
