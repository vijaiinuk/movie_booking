package org.example.booking.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BookingRequest {
    @NotBlank(message = "Movie Name Cannot be blank")
    private String movieName;
    @Min(value = 1, message = "Invalid no of seats requested")
    @Max(value = 4, message = "Maximum of 4 tickets per booking")
    private int noOfTickets;
}
