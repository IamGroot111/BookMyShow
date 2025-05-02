package com.BookingSystem.BookMyShow.service;

import com.BookingSystem.BookMyShow.model.Booking;
import com.BookingSystem.BookMyShow.model.Show;
import com.BookingSystem.BookMyShow.model.Slot;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingService {

    // Map to store shows with their name as key
    private Map<String, Show> shows = new HashMap<>();

    // Map to store user bookings with user name as key
    private Map<String, List<Booking>> userBookings = new HashMap<>();

    private Map<String, List<Booking>> showBookings = new HashMap<>();
    // Map to store booking ids for tracking
    private Map<String, Booking> bookings = new HashMap<>();

    private final Map<String, Queue<Booking>> waitlists = new HashMap<>();

    // Method to register a new show
    public void registerShow(String showName, String genre) {
        if (!shows.containsKey(showName)) {
            Show newShow = new Show(showName, genre);
            shows.put(showName, newShow);
            System.out.println(showName + " show is registered !!");
        } else {
            System.out.println("Show " + showName + " already exists.");
        }
    }

    // Method to onboard new time slots for a show
    public void onboardShowSlots(String showName, String timeSlot, int capacity) {
        Show show = shows.get(showName);
        if (show == null) {
            System.out.println("Show " + showName + " does not exist.");
            return;
        }

        // Split timeSlot to get start time and end time (Just checking the format, assuming 1 hour slots)
        String[] timeParts = timeSlot.split("-");
        if (timeParts.length != 2) {
            System.out.println("Invalid time slot format. It should be HH:mm-HH:mm");
            return;
        }

        // Add the slot to the show
        Slot newSlot = new Slot(timeSlot, capacity);
        show.addSlot(newSlot);
        System.out.println("Done!");
    }

    // Method to book tickets for a user
    public String bookTicket(String userName, String showName, String timeSlot, int tickets) {
        Show show = shows.get(showName);
        if (show == null) {
            System.out.println("Show not found.");
            return null;
        }
        for(Booking b: userBookings.getOrDefault(userName,Collections.emptyList()))
        {
            if(b.getTimeSlot().equals(timeSlot)){
                System.out.println("Booking Failed! User already has another booking during this time interval.");
                return null;
            }
        }
        Slot slot = show.getSlot(timeSlot);
        if (slot == null) {
            System.out.println("Slot not available.");
            return null;
        }

        String bookingId = UUID.randomUUID().toString();
        Booking booking = new Booking(bookingId, userName, showName, timeSlot, tickets);
        bookings.put(bookingId, booking);
        

        if (slot.getAvailableCapacity() < tickets) {
            String keySlot = showName + " " + timeSlot;
            waitlists.computeIfAbsent(keySlot,k -> new LinkedList<>()).add(booking);
            booking.setWaitListed(true);
            booking.setConfirmed(false);
            System.out.println("Not enough capacity for this slot. Added to waitlist.");
        }

        // Deduct capacity
        else{
        userBookings.computeIfAbsent(userName, k -> new ArrayList<>()).add(booking);
        showBookings.computeIfAbsent(showName, k -> new ArrayList<>()).add(booking);
        booking.setConfirmed(true);
        booking.setWaitListed(false);
        slot.setAvailableCapacity(slot.getAvailableCapacity() - tickets);
        System.out.println("Booking Confirmed! Booking id: " + bookingId);
        }
        return bookingId;
    }


    // Method to cancel a booking
    public void cancelBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            System.out.println("Booking not found.");
            return;
        }

        // Revert the slot capacity
        Show show = shows.get(booking.getShowName());
        Slot slot = show.getSlot(booking.getTimeSlot());
        
        slot.setAvailableCapacity(slot.getAvailableCapacity() + booking.getTickets());
        // Remove booking
        userBookings.get(booking.getUserName()).remove(booking);
        bookings.remove(bookingId);

        System.out.println("Booking Cancelled");
        String keySlot = booking.getShowName() + " " + booking.getTimeSlot();
        if(waitlists.containsKey(keySlot))
        {
            while(!waitlists.get(keySlot).isEmpty()) {
                Booking nextBooking = waitlists.get(keySlot).peek();
                int persons = nextBooking.getTickets();
                int capacity = slot.getAvailableCapacity();
                if(persons <= capacity)
                {
                    waitlists.get(keySlot).poll();
                    userBookings.computeIfAbsent(nextBooking.getUserName(), k -> new ArrayList<>()).add(booking);
                    showBookings.computeIfAbsent(nextBooking.getShowName(), k -> new ArrayList<>()).add(booking);
                    nextBooking.setConfirmed(true);
                    nextBooking.setWaitListed(false);
                    slot.setAvailableCapacity(slot.getAvailableCapacity() - nextBooking.getTickets());
                    System.out.println("Booking Confirmed! Booking id: " + bookingId);
                }
                else
                {
                    break;
                }
            }
        }
    }

    // Method to get available shows by genre
    public String showAvailByGenre(String genre) {
        StringBuilder availableShows = new StringBuilder();
        for (Show show : shows.values()) {
            if (show.getGenre().equalsIgnoreCase(genre)) {
                availableShows.append(show.getShowName()).append(": ");
                for (Slot slot : show.getSlots()) {
                    availableShows.append("(").append(slot.getTimeSlot()).append(") ").append(slot.getAvailableCapacity()).append(" ");
                }
                availableShows.append("\n");
            }
        }
        return availableShows.toString();
    }

    // Method to return the trending show with the most bookings
    public String showTrendingShow() {
        Map<String, Integer> showBookingCounts = new HashMap<>();
        for (Booking booking : bookings.values()) {
            showBookingCounts.put(booking.getShowName(), showBookingCounts.getOrDefault(booking.getShowName(), 0) + booking.getTickets());
        }

        String trendingShow = null;
        int maxBookings = 0;
        for (Map.Entry<String, Integer> entry : showBookingCounts.entrySet()) {
            if (entry.getValue() > maxBookings) {
                maxBookings = entry.getValue();
                trendingShow = entry.getKey();
            }
        }
        return trendingShow;
    }

    public List<Booking> getBookingsForShow(String showName) {
        return showBookings.getOrDefault(showName, new ArrayList<>());
    }
    
    // Helper method to check if the show is present in the system
    public boolean isShowPresent(String showName) {
        return shows.containsKey(showName);
    }

    // Helper method to check if a slot is available for booking
    public boolean isSlotAvailable(String showName, String timeSlot) {
        Show show = shows.get(showName);
        if (show == null) return false;

        Slot slot = show.getSlot(timeSlot);
        return slot != null && slot.getAvailableCapacity() > 0;
    }

    // Helper method to check if a booking is confirmed
    public boolean isBookingConfirmed(String bookingId) {
        if(!bookings.containsKey(bookingId))
            return false;
        return bookings.get(bookingId).isConfirmed();
    }

    public boolean isBookingWaitlisted(String bookingId) {
        return bookings.get(bookingId).isWaitListed();
    }
    public void clearData() {
        shows.clear();
        bookings.clear();
        userBookings.clear();
        showBookings.clear();
        waitlists.clear();
    }
    
    
}
