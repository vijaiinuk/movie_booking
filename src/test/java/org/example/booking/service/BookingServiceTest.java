package org.example.booking.service;

import org.example.booking.config.MovieConfiguration;
import org.example.booking.dto.BookingRequest;
import org.example.booking.dto.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private MovieConfiguration configuration;

    @Test
    void validBookingCount() {
        when(configuration.getMinTicketCount()).thenReturn(1);
        when(configuration.getMaxTicketCount()).thenReturn(2);

        assertTrue(bookingService.validBookingCount(1));
        assertTrue(bookingService.validBookingCount(2));

        assertFalse(bookingService.validBookingCount(0));
        assertFalse(bookingService.validBookingCount(4));
    }

    @Test
    void getSelectedMovie() {

        Movie spiderman = Movie.builder().name("Spiderman").available(2).pricePerSeat(10).taxPercentagePerSeat(10).build();
        Map<String, Movie> movies = Map.of("Spiderman", spiderman);
        when(configuration.movies()).thenReturn(movies);
        assertNotNull(bookingService.getSelectedMovie("Spiderman"));
        assertEquals(spiderman, bookingService.getSelectedMovie("Spiderman"));

        assertNull(bookingService.getSelectedMovie("X Men"));
    }

    @Test
    void doBooking() {
        Movie spiderman = Movie.builder().name("Spiderman").available(2).pricePerSeat(10).taxPercentagePerSeat(10).build();
        BookingRequest bookingRequest = BookingRequest.builder()
                .movieName("Spiderman")
                .noOfTickets(1).build();

        assertTrue(bookingService.doBooking(spiderman, bookingRequest));

        //Not enough seats available
        BookingRequest bookingRequest2 = BookingRequest.builder()
                .movieName("Spiderman")
                .noOfTickets(2).build();
        assertFalse(bookingService.doBooking(spiderman, bookingRequest2));

    }
}