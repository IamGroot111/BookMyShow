package com.BookingSystem.BookMyShow;
import com.BookingSystem.BookMyShow.model.Booking;
// import com.BookingSystem.BookMyShow.model.Slot;
import com.BookingSystem.BookMyShow.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookMyShowApplicationTests {

    @Autowired
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        System.out.println("\n---- Setting up test data ----");
        bookingService.clearData();
    }

    @Test
    void testRegisterShow() {
        System.out.println("Running testRegisterShow...");
        bookingService.registerShow("TMKOC", "Comedy");
        assertTrue(bookingService.isShowPresent("TMKOC"));
        System.out.println("✅ testRegisterShow passed");
    }

    @Test
    void testOnboardShowSlots() {
        System.out.println("Running testOnboardShowSlots...");
        bookingService.registerShow("TMKOC", "Comedy");
        bookingService.onboardShowSlots("TMKOC", "09:00-10:00", 3);

        assertTrue(bookingService.isSlotAvailable("TMKOC", "09:00-10:00"));
        System.out.println("✅ testOnboardShowSlots passed");
    }

    @Test
    void testBookTicketConfirmed() {
        System.out.println("Running testBookTicketConfirmed...");
        bookingService.registerShow("TMKOC", "Comedy");
        bookingService.onboardShowSlots("TMKOC", "09:00-10:00", 5);

        String id = bookingService.bookTicket("UserA", "TMKOC", "09:00-10:00", 2);
        assertNotNull(id);
        assertTrue(bookingService.isBookingConfirmed(id));
        System.out.println("✅ testBookTicketConfirmed passed");
    }

    @Test
    void testGetBookingsForShow() {
        System.out.println("Running testGetBookingsForShow...");
        bookingService.registerShow("TMKOC", "Comedy");
        bookingService.onboardShowSlots("TMKOC", "09:00-10:00", 5);

        String id1 = bookingService.bookTicket("User1", "TMKOC", "09:00-10:00", 2);
        String id2 = bookingService.bookTicket("User2", "TMKOC", "09:00-10:00", 2);

        List<Booking> bookings = bookingService.getBookingsForShow("TMKOC");
        assertEquals(2, bookings.size());
        assertTrue(bookings.stream().anyMatch(b -> b.getBookingId().equals(id1)));
        assertTrue(bookings.stream().anyMatch(b -> b.getBookingId().equals(id2)));
        System.out.println("✅ testGetBookingsForShow passed");
    }

    @Test
    void testCancelBooking() {
        System.out.println("Running testCancelBooking...");
        bookingService.registerShow("TMKOC", "Comedy");
        bookingService.onboardShowSlots("TMKOC", "09:00-10:00", 3);
        String id = bookingService.bookTicket("UserA", "TMKOC", "09:00-10:00", 2);

        bookingService.cancelBooking(id);
        assertFalse(bookingService.isBookingConfirmed(id));
        System.out.println("✅ testCancelBooking passed");
    }

    @Test
    void testShowAvailByGenre() {
    System.out.println("Running testShowAvailByGenre...");
    bookingService.registerShow("TMKOC", "Comedy");
    bookingService.onboardShowSlots("TMKOC", "09:00-10:00", 4);

    List<String> showNames = bookingService.showAvailByGenre("Comedy");

    assertEquals(1, showNames.size(), "There should be one show available in Comedy genre");
    assertEquals("TMKOC", showNames.get(0), "The available show should be TMKOC");
    System.out.println("✅ testShowAvailByGenre passed");
}

    @Transactional
    @Test
    void testTrendingShow() {
        System.out.println("Running testTrendingShow...");
        bookingService.registerShow("TMKOC", "Comedy");
        bookingService.onboardShowSlots("TMKOC", "09:00-10:00", 5);
        bookingService.bookTicket("UserA", "TMKOC", "09:00-10:00", 3);

        Optional<String> trending = bookingService.showTrendingShow();
        assertTrue(trending.isPresent());
        assertEquals("TMKOC", trending.get());
        System.out.println("✅ testTrendingShow passed");
    }

    @Test
    void testWaitlistWhenSlotFull() {
        System.out.println("Running testWaitlistWhenSlotFull...");
        bookingService.registerShow("BBT", "Comedy");
        bookingService.onboardShowSlots("BBT", "18:00-19:00", 2);

        String id1 = bookingService.bookTicket("User1", "BBT", "18:00-19:00", 1);
        String id2 = bookingService.bookTicket("User2", "BBT", "18:00-19:00", 1);
        String id3 = bookingService.bookTicket("User3", "BBT", "18:00-19:00", 1);

        assertTrue(bookingService.isBookingConfirmed(id1));
        assertTrue(bookingService.isBookingConfirmed(id2));
        assertFalse(bookingService.isBookingConfirmed(id3));
        assertTrue(bookingService.isBookingWaitlisted(id3));
        System.out.println("✅ testWaitlistWhenSlotFull passed");
    }

    @Test
    void testWaitlistPromotion() {
        System.out.println("Running testWaitlistPromotion...");
        bookingService.registerShow("BBT", "Comedy");
        bookingService.onboardShowSlots("BBT", "18:00-19:00", 2);

        String id1 = bookingService.bookTicket("User1", "BBT", "18:00-19:00", 1);
        String id2 = bookingService.bookTicket("User2", "BBT", "18:00-19:00", 1);
        String id3 = bookingService.bookTicket("User3", "BBT", "18:00-19:00", 1);

        bookingService.cancelBooking(id2); // frees up 1 seat

        assertTrue(bookingService.isBookingConfirmed(id3));
        System.out.println("✅ testWaitlistPromotion passed");
    }

    @Test
    void testUserCannotDoubleBookSameTime() {
        System.out.println("Running testUserCannotDoubleBookSameTime...");
        bookingService.registerShow("Show1", "Drama");
        bookingService.onboardShowSlots("Show1", "14:00-15:00", 2);
        bookingService.registerShow("Show2", "Action");
        bookingService.onboardShowSlots("Show2", "14:00-15:00", 2);

        String id1 = bookingService.bookTicket("UserX", "Show1", "14:00-15:00", 1);
        Exception ex = assertThrows(IllegalStateException.class, () ->
            bookingService.bookTicket("UserX", "Show2", "14:00-15:00", 1)
        );
        assertTrue(ex.getMessage().contains("already has a booking"));
        System.out.println("✅ testUserCannotDoubleBookSameTime passed");
    }
}
