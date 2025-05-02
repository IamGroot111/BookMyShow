package com.BookingSystem.BookMyShow.model;

public class Booking {

    private String bookingId;
    private String userName;
    private String showName;
    private String timeSlot;
    private int tickets;
    private boolean isConfirmed;
    

    private boolean isWaitListed;

    

    public Booking(String bookingId, String userName, String showName, String timeSlot, int tickets) {
        this.bookingId = bookingId;
        this.userName = userName;
        this.showName = showName;
        this.timeSlot = timeSlot;
        this.tickets = tickets;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }
    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public boolean isWaitListed() {
        return isWaitListed;
    }

    public void setWaitListed(boolean isWaitListed) {
        this.isWaitListed = isWaitListed;
    }
}
