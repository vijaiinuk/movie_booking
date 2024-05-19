package org.example.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.example.booking.dto.Booking;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ReportScheduler {

    private final Map<String, List<Booking>> bookings;


    public ReportScheduler(Map<String, List<Booking>> bookings) {
        this.bookings = bookings;
    }

    /**
     *  Total revenue (including tax) across all movies
     * - Movie that has got the highest revenue (including tax) in a single booking
     * For each movie print
     * - Movie Name:
     * - Total revenue (including tax) across all bookings:
     * - Bookings: List of bookings with pipe or comma separated data table like:
     */


    @Scheduled(fixedRateString = "${report.interval.millis}")
    public void report() {

        double totalRevenue = bookings.values().stream()
                .flatMap(List::stream).mapToDouble(b -> b.getTotalPrice()+b.getTotalTax()).sum();
        log.info("Total Revenue so far: "+totalRevenue);
        highestBookingMovie();
        log.info("Total Revenue per Movie: ");
        totalRevenuePerMovie();
    }

    private void highestBookingMovie() {
        Booking highestBooking = bookings.values().stream().flatMap(List::stream)
                .max(Comparator.comparingDouble(b -> b.getTotalPrice() + b.getTotalTax()))
                .orElse(Booking.builder().build());
        log.info("Movie that has got the highest revenue (including tax) in a single booking: {}", highestBooking);
    }


    private void totalRevenuePerMovie() {
        Map<String, Double> revenuePerMovie = bookings.values().stream().flatMap(List::stream)
                .collect(Collectors.toMap(Booking::getMovieName, b -> b.getTotalPrice() + b.getTotalTax(), Double::sum));

        revenuePerMovie.entrySet().forEach(e -> {
            log.info("Movie Name: "+e.getKey());
            log.info("Total revenue (including tax) across all bookings: "+e.getValue());
            bookings.get(e.getKey()).forEach(b -> {
                log.info("{} | {} | {}", b.getBookingDate(), b.getTickets(), b.getTotalPrice()+b.getTotalTax());
            });
        });
    }
}
