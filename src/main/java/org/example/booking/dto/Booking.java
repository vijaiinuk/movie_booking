package org.example.booking.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Booking {

    private String movieName;
    private String bookingDate;
    private int tickets;
    private double totalPrice;
    private double totalTax;

}
