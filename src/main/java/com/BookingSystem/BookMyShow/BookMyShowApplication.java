package com.BookingSystem.BookMyShow;

// import com.BookingSystem.BookMyShow.service.BookingService;
// import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookMyShowApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookMyShowApplication.class, args);
    }

    // @Bean
    // public CommandLineRunner run(BookingService bookingService) {
    //     return args -> {
    //         bookingService.registerShow("TMKOC", "Comedy");
    //         bookingService.onboardShowSlots("TMKOC", "9:00-10:00", 3);
    //         bookingService.onboardShowSlots("TMKOC", "12:00-13:00", 2);
    //         bookingService.onboardShowSlots("TMKOC", "15:00-16:00", 5);

    //         bookingService.registerShow("The Sonu Nigam Live Event", "Singing");
    //         bookingService.onboardShowSlots("The Sonu Nigam Live Event", "10:00-11:00", 3);
    //         bookingService.onboardShowSlots("The Sonu Nigam Live Event", "12:00-13:00", 2);
    //         bookingService.onboardShowSlots("The Sonu Nigam Live Event", "17:00-18:00", 1);

    //         bookingService.showAvailByGenre("Comedy");

    //         String bookingId1 = bookingService.bookTicket("UserA", "TMKOC", "12:00-13:00", 2);
    //         String bookingId2 = bookingService.bookTicket("UserA", "The Sonu Nigam Live Event", "12:00-13:00", 2);
            
    //         System.out.println(bookingService.showAvailByGenre("Comedy"));

    //         bookingService.bookTicket("UserB", "TMKOC", "12:00-13:00", 2);
    //         bookingService.cancelBooking(bookingId1);
    //         bookingService.registerShow("The Arijit Singh Live Event", "Singing");
    //         bookingService.onboardShowSlots("The Arijit Singh Live Event", "11:00-12:00", 3);
    //         bookingService.onboardShowSlots("The Arijit Singh Live Event", "14:00-15:00", 2);

    //         System.out.println(bookingService.showAvailByGenre("Singing"));

    //         System.out.println(bookingService.showTrendingShow()); // Bonus feature
    // };
    // }
}
