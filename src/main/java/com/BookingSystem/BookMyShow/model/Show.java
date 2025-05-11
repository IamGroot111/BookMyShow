package com.BookingSystem.BookMyShow.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class Show {

    @Id
    private String name;

    private String genre;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Slot> slots = new ArrayList<>();

    // Constructors, Getters, Setters
    public Show() {}

    public Show(String name, String genre) {
        this.name = name;
        this.genre = genre;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getGenre() { return genre; }

    public void setGenre(String genre) { this.genre = genre; }

    public List<Slot> getSlots() { return slots; }

    public void setSlots(List<Slot> slots) { this.slots = slots; }
}
