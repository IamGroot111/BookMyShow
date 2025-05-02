package com.BookingSystem.BookMyShow.model;

import java.util.ArrayList;
import java.util.List;

public class Show {

    private String showName;
    private String genre;
    private List<Slot> slots;

    public Show(String showName, String genre) {
        this.showName = showName;
        this.genre = genre;
        this.slots = new ArrayList<>();
    }

    public void addSlot(Slot slot) {
        this.slots.add(slot);
    }

    public Slot getSlot(String timeSlot) {
        for (Slot slot : slots) {
            if (slot.getTimeSlot().equals(timeSlot)) {
                return slot;
            }
        }
        return null;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }
}
