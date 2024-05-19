package org.example.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    private String name;
    private int available;
    private double pricePerSeat;
    private double taxPercentagePerSeat;

    public boolean reduceBooking(int noOfTickets)  {
        if(available >= noOfTickets) {
            available -= noOfTickets;
            return true;
        } else
            return false;

    }

}
