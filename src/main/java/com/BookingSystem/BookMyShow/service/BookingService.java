package com.BookingSystem.BookMyShow.service;

import com.BookingSystem.BookMyShow.model.Booking;
import com.BookingSystem.BookMyShow.model.Show;
import com.BookingSystem.BookMyShow.model.Slot;
import com.BookingSystem.BookMyShow.repository.BookingRepository;
import com.BookingSystem.BookMyShow.repository.ShowRepository;
import com.BookingSystem.BookMyShow.repository.SlotRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private BookingRepository bookingRepository;

    /** Register a new show */
    public void registerShow(String name, String genre) {
        if (showRepository.existsById(name)) {
            throw new IllegalArgumentException("Show already exists: " + name);
        }
        Show show = new Show(name, genre);
        showRepository.save(show);
    }

    /** Onboard one-hour slots for a show */
    @Transactional
    public void onboardShowSlots(String showName, String timeRange, int capacity) {
        Show show = showRepository.findById(showName)
            .orElseThrow(() -> new NoSuchElementException("Show not found: " + showName));

        // Validate 1-hour slot format
        String[] parts = timeRange.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Slot must be HH:mm-HH:mm");
        }

        // Ensure no duplicate slot
        slotRepository.findByShowAndTimeRange(show, timeRange)
            .ifPresent(s -> { throw new IllegalArgumentException("Slot already exists: " + timeRange); });

        Slot slot = new Slot(timeRange, capacity, show);
        slotRepository.save(slot);
    }

    /** Book tickets (or waitlist if full), preventing double-booking */
    @Transactional
    public String bookTicket(String user, String showName, String timeRange, int numTickets) {
        // 1. Load slot
        Show show = showRepository.findById(showName)
            .orElseThrow(() -> new NoSuchElementException("Show not found: " + showName));
        Slot slot = slotRepository.findByShowAndTimeRange(show, timeRange)
            .orElseThrow(() -> new NoSuchElementException("Slot not found: " + timeRange));

        // 2. Prevent double-booking
        List<Booking> userBookings = bookingRepository.findByUsername(user);
        for (Booking b : userBookings) {
            if (b.getSlot().getTimeRange().equals(timeRange) && b.isConfirmed()) {
                throw new IllegalStateException("User already has a booking at " + timeRange);
            }
        }

        // 3. Create booking record
        String bookingId = UUID.randomUUID().toString();
        Booking booking = new Booking(
            bookingId,
            user,
            numTickets,
            false,   // confirmed?
            false,   // waitlisted?
            slot
        );

        // 4. Check capacity
        if (slot.getCapacity() >= numTickets) {
            // Confirm booking
            slot.setCapacity(slot.getCapacity() - numTickets);
            booking.setConfirmed(true);
        } else {
            // Add to waitlist
            booking.setWaitlisted(true);
        }

        slotRepository.save(slot);
        bookingRepository.save(booking);
        return bookingId;
    }

    /** Cancel a confirmed booking and promote waitlisted users */
    @Transactional
    public void cancelBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new NoSuchElementException("Booking not found: " + bookingId));

        Slot slot = booking.getSlot();

        if (booking.isConfirmed()) {
            // Release capacity
            slot.setCapacity(slot.getCapacity() + booking.getNumTickets());
            slotRepository.save(slot);

            // Promote first waitlisted booking, if any
            List<Booking> waitlisted = bookingRepository.findBySlot(slot);
            waitlisted.stream()
                .filter(Booking::isWaitlisted)
                .sorted(Comparator.comparing(Booking::getBookingId)) // FIFO by ID timestamp
                .findFirst()
                .ifPresent(next -> {
                    if (slot.getCapacity() >= next.getNumTickets()) {
                        next.setWaitlisted(false);
                        next.setConfirmed(true);
                        slot.setCapacity(slot.getCapacity() - next.getNumTickets());
                        slotRepository.save(slot);
                        bookingRepository.save(next);
                    }
                });
        }

        // Remove original booking
        bookingRepository.delete(booking);
    }

   public List<String> showAvailByGenre(String genre) {
    List<Show> shows = showRepository.findByGenre(genre);
    return shows.stream()
                .map(Show::getName)
                .collect(Collectors.toList());
}
    

    /** Get trending show (most tickets confirmed) */
    public Optional<String> showTrendingShow() {
        Map<String, Integer> counts = new HashMap<>();
        List<Booking> all = bookingRepository.findAll();
        for (Booking b : all) {
            if (b.isConfirmed()) {
                String name = b.getSlot().getShow().getName();
                counts.put(name, counts.getOrDefault(name, 0) + b.getNumTickets());
            }
        }
        return counts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey);
    }

    /** Retrieve all bookings for a given show */
    public List<Booking> getBookingsForShow(String showName) {
        Show show = showRepository.findById(showName)
            .orElseThrow(() -> new NoSuchElementException("Show not found: " + showName));
        List<Booking> out = new ArrayList<>();
        for (Slot slot : slotRepository.findByShow(show)) {
            out.addAll(bookingRepository.findBySlot(slot));
        }
        return out;
    }

    /** Utility helpers */

    public boolean isShowPresent(String showName) {
        return showRepository.existsById(showName);
    }

    public boolean isSlotAvailable(String showName, String timeRange) {
        return showRepository.findById(showName)
            .flatMap(show -> slotRepository.findByShowAndTimeRange(show, timeRange))
            .map(s -> s.getCapacity() > 0)
            .orElse(false);
    }

    public boolean isBookingConfirmed(String bookingId) {
        return bookingRepository.findById(bookingId)
            .map(Booking::isConfirmed)
            .orElse(false);
    }

    public boolean isBookingWaitlisted(String bookingId) {
        return bookingRepository.findById(bookingId)
            .map(Booking::isWaitlisted)
            .orElse(false);
    }
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }
    
    public List<Slot> getSlotsForShow(String name) {
        return slotRepository.findByShow_Name(name);
    }
    
    /** Clear all data (testing only) */
    @Transactional
    public void clearData() {
        bookingRepository.deleteAll();
        slotRepository.deleteAll();
        showRepository.deleteAll();
    }
}
