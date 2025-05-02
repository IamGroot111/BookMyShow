package com.BookingSystem.BookMyShow;

import com.BookingSystem.BookMyShow.model.Booking;
import com.BookingSystem.BookMyShow.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
class BookMyShowApplicationTests {

    @Autowired
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        System.out.println();
        System.out.println("---- Setting up test data ----");
        // Clear data if needed
        bookingService.clearData();
    }

    @Test
    void testRegisterShow() {
        System.out.println("Running testRegisterShow...");
        bookingService.registerShow("TMKOC", "Comedy");

        assertTrue(bookingService.isShowPresent("TMKOC"), "The show TMKOC should be registered");
        System.out.println("✅ testRegisterShow passed");
    }

    @Test
    void testOnboardShowSlots() {
        System.out.println("Running testOnboardShowSlots...");
        bookingService.registerShow("TMKOC", "Comedy");
        bookingService.onboardShowSlots("TMKOC", "9:00-10:00", 3);

        assertTrue(bookingService.isSlotAvailable("TMKOC", "9:00-10:00"), "The slot 9:00-10:00 should be available");
        System.out.println("✅ testOnboardShowSlots passed");
    }

    @Test
    void testBookTicket() {
        System.out.println("Running testBookTicket...");
        bookingService.registerShow("TMKOC", "Comedy");
        bookingService.onboardShowSlots("TMKOC", "9:00-10:00", 3);

        String bookingId = bookingService.bookTicket("UserA", "TMKOC", "9:00-10:00", 2);

        assertNotNull(bookingId, "Booking ID should not be null");
        assertTrue(bookingService.isBookingConfirmed(bookingId), "Booking should be confirmed for UserA");
        System.out.println("✅ testBookTicket passed");
    }

    @Test
    void testGetBookingsForShow() {
        System.out.println("Running testGetBookingsForShow...");
        bookingService.registerShow("TMKOC", "Comedy");
        bookingService.onboardShowSlots("TMKOC", "9:00-10:00", 5);

        String bookingId1 = bookingService.bookTicket("User1", "TMKOC", "9:00-10:00", 2);
        String bookingId2 = bookingService.bookTicket("User2", "TMKOC", "9:00-10:00", 2);

        List<Booking> bookings = bookingService.getBookingsForShow("TMKOC");

        assertEquals(2, bookings.size(), "There should be 2 bookings for TMKOC");
        assertTrue(bookings.stream().anyMatch(b -> b.getBookingId().equals(bookingId1)), "BookingId1 should exist");
        assertTrue(bookings.stream().anyMatch(b -> b.getBookingId().equals(bookingId2)), "BookingId2 should exist");
        System.out.println("✅ testGetBookingsForShow passed");
    }

    @Test
    void testCancelBooking() {
        System.out.println("Running testCancelBooking...");
        bookingService.registerShow("TMKOC", "Comedy");
        bookingService.onboardShowSlots("TMKOC", "9:00-10:00", 3);
        String bookingId = bookingService.bookTicket("UserA", "TMKOC", "9:00-10:00", 2);

        bookingService.cancelBooking(bookingId);

        assertFalse(bookingService.isBookingConfirmed(bookingId), "Booking should be cancelled");
        System.out.println("✅ testCancelBooking passed");
    }

    @Test
    void testShowAvailByGenre() {
        System.out.println("Running testShowAvailByGenre...");
        bookingService.registerShow("TMKOC", "Comedy");
        bookingService.onboardShowSlots("TMKOC", "9:00-10:00", 3);

        String availableShows = bookingService.showAvailByGenre("Comedy");

        assertTrue(availableShows.contains("TMKOC"), "Should list TMKOC for Comedy genre");
        System.out.println("✅ testShowAvailByGenre passed");
    }

    @Test
    void testTrendingShow() {
        System.out.println("Running testTrendingShow...");
        bookingService.registerShow("TMKOC", "Comedy");
        bookingService.onboardShowSlots("TMKOC", "9:00-10:00", 3);
        bookingService.bookTicket("UserA", "TMKOC", "9:00-10:00", 2);

        String trending = bookingService.showTrendingShow();

        assertEquals("TMKOC", trending, "Trending show should be TMKOC");
        System.out.println("✅ testTrendingShow passed");
    }

    @Test
    void testWaitlistWhenSlotFull() {
        System.out.println("Running testWaitlistWhenSlotFull...");
        bookingService.registerShow("BBT", "Comedy");
        bookingService.onboardShowSlots("BBT", "18:00-19:00", 2);

        String id1 = bookingService.bookTicket("User1", "BBT", "18:00-19:00", 1);
        String id2 = bookingService.bookTicket("User2", "BBT", "18:00-19:00", 1);
        String id3 = bookingService.bookTicket("User3", "BBT", "18:00-19:00", 1); // Should go to waitlist

        assertTrue(bookingService.isBookingConfirmed(id1));
        assertTrue(bookingService.isBookingConfirmed(id2));
        assertFalse(bookingService.isBookingConfirmed(id3));
        assertTrue(bookingService.isBookingWaitlisted(id3));
        System.out.println("✅ testWaitlistWhenSlotFull passed");
    }

    @Test
    void testWaitlistedGetsConfirmedAfterCancellation() {
        System.out.println("Running testWaitlistedGetsConfirmedAfterCancellation...");
        bookingService.registerShow("BBT", "Comedy");
        bookingService.onboardShowSlots("BBT", "18:00-19:00", 2);

        String id1 = bookingService.bookTicket("User1", "BBT", "18:00-19:00", 1);
        String id2 = bookingService.bookTicket("User2", "BBT", "18:00-19:00", 1);
        String id3 = bookingService.bookTicket("User3", "BBT", "18:00-19:00", 1); // Waitlisted

        bookingService.cancelBooking(id2); // Now User3 should get confirmed

        assertTrue(bookingService.isBookingConfirmed(id3), "User3 should be confirmed after cancellation");
        System.out.println("✅ testWaitlistedGetsConfirmedAfterCancellation passed");
    }

    @Test
    void testUserCannotDoubleBookSameTime() {
        System.out.println("Running testUserCannotDoubleBookSameTime...");
        bookingService.registerShow("Show1", "Drama");
        bookingService.onboardShowSlots("Show1", "14:00-15:00", 2);

        bookingService.registerShow("Show2", "Action");
        bookingService.onboardShowSlots("Show2", "14:00-15:00", 2);

        String id1 = bookingService.bookTicket("UserX", "Show1", "14:00-15:00", 1);
        String id2 = bookingService.bookTicket("UserX", "Show2", "14:00-15:00", 1); // Should fail due to time clash

        assertNotNull(id1);
        assertNull(id2, "UserX should not be able to book two shows at the same time");
        System.out.println("✅ testUserCannotDoubleBookSameTime passed");
    }
}
