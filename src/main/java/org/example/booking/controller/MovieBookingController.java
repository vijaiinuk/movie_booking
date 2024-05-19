package org.example.booking.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.booking.dto.BookingRequest;
import org.example.booking.dto.Movie;
import org.example.booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MovieBookingController {

    private final BookingService service;

    public MovieBookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping("/v1/booking")
    public ResponseEntity<String> booking(@Valid @RequestBody BookingRequest bookingRequest) {
        log.info("Booking request: {}", bookingRequest);

        if(!service.validBookingCount(bookingRequest.getNoOfTickets())) {
            return ResponseEntity.status(400).body("You can book 1 to 4 tickets maximum");

        }

        Movie selectedMovie = service.getSelectedMovie(bookingRequest.getMovieName());

        //check movie name exists
        if(selectedMovie == null) {
            return ResponseEntity.status(400).body("Movie "+ bookingRequest.getMovieName() +" not found");
        }

        if(selectedMovie.getAvailable() < bookingRequest.getNoOfTickets()) {
            return ResponseEntity.status(200).body("Not enough seats are available for " + bookingRequest.getMovieName());
        }

        if(service.doBooking(selectedMovie, bookingRequest)) {
            return ResponseEntity.status(200).body(bookingRequest.getNoOfTickets()+
                    " seats booked successfully for "+bookingRequest.getMovieName());
        } else
            return ResponseEntity.status(400).body("Not enough seats are available for" + bookingRequest.getMovieName());

    }

}
