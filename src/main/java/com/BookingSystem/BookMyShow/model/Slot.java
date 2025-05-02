package com.BookingSystem.BookMyShow.model;

public class Slot {

    private String timeSlot;
    private int availableCapacity;

    public Slot(String timeSlot, int availableCapacity) {
        this.timeSlot = timeSlot;
        this.availableCapacity = availableCapacity;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(int availableCapacity) {
        this.availableCapacity = availableCapacity;
    }
}
