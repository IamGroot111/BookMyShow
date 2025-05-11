package com.BookingSystem.BookMyShow.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.*;

@Entity
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String timeRange;

    private int capacity;

    @ManyToOne
    @JoinColumn(name = "show_name")
    @JsonIgnore
    private Show show;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    // Constructors, Getters, Setters
    public Slot() {}

    public Slot(String timeRange, int capacity, Show show) {
        this.timeRange = timeRange;
        this.capacity = capacity;
        this.show = show;
    }

    public Long getId() { return id; }

    public String getTimeRange() { return timeRange; }

    public void setTimeRange(String timeRange) { this.timeRange = timeRange; }

    public int getCapacity() { return capacity; }

    public void setCapacity(int capacity) { this.capacity = capacity; }

    public Show getShow() { return show; }

    public void setShow(Show show) { this.show = show; }

    public List<Booking> getBookings() { return bookings; }

    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}
