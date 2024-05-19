package org.example.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.example.booking.config.MovieConfiguration;
import org.example.booking.dto.Booking;
import org.example.booking.dto.BookingRequest;
import org.example.booking.dto.Movie;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BookingService {

    private final MovieConfiguration configuration;

    public BookingService(MovieConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean validBookingCount(int noOfTickets) {
        return noOfTickets >= configuration.getMinTicketCount() && noOfTickets <= configuration.getMaxTicketCount();
    }

    public Movie getSelectedMovie(String movieName) {
        return configuration.movies().get(movieName);
    }

    public boolean doBooking(Movie selectedMovie, BookingRequest bookingRequest)  {
        //reduce the no of seats from db
        boolean result = selectedMovie.reduceBooking(bookingRequest.getNoOfTickets());
        if(result) {
            double tax = selectedMovie.getTaxPercentagePerSeat() * selectedMovie.getPricePerSeat() * bookingRequest.getNoOfTickets() / 100;
            Booking.BookingBuilder bookingBuilder = Booking.builder().bookingDate(LocalDateTime.now().toString())
                    .movieName(bookingRequest.getMovieName())
                    .tickets(bookingRequest.getNoOfTickets())
                    .totalPrice(selectedMovie.getPricePerSeat() * bookingRequest.getNoOfTickets())
                    .totalTax(tax);
            log.info("Booking result: {}", bookingBuilder.build());
            List<Booking> bookingList = configuration.bookings().getOrDefault(bookingRequest.getMovieName(), new ArrayList<>());
            bookingList.add(bookingBuilder.build());
            configuration.bookings().put(bookingRequest.getMovieName(), bookingList);

            return true;
        } else
            return false;

    }
}
