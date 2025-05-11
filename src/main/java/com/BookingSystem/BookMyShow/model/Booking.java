package com.BookingSystem.BookMyShow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Booking {

    @Id
    private String bookingId;

    private String username;

    // Renamed for consistency with service: numTickets instead of seatCount
    private int numTickets;

    private boolean confirmed;

    private boolean waitlisted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    @JsonIgnore
    private Slot slot;

    public Booking() {}

    public Booking(String bookingId,
                   String username,
                   int numTickets,
                   boolean confirmed,
                   boolean waitlisted,
                   Slot slot) {
        this.bookingId = bookingId;
        this.username = username;
        this.numTickets = numTickets;
        this.confirmed = confirmed;
        this.waitlisted = waitlisted;
        this.slot = slot;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isWaitlisted() {
        return waitlisted;
    }

    public void setWaitlisted(boolean waitlisted) {
        this.waitlisted = waitlisted;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }
}
