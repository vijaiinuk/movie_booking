package org.example.booking.controller;

import org.example.booking.dto.Movie;
import org.example.booking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieBookingController.class)
class MovieBookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService service;

    @Test
    void test_booking_invalid_request() throws Exception {
        mockMvc.perform(post("/v1/booking"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void test_booking_invalid_seatcount() throws Exception {
        when(service.validBookingCount(anyInt())).thenReturn(false);
        when(service.getSelectedMovie(anyString()))
                .thenReturn(
                        Movie.builder()
                                .name("Spiderman")
                                .pricePerSeat(20)
                                .available(5)
                                .taxPercentagePerSeat(15).build()
                );
        when(service.doBooking(any(), any())).thenReturn(true);

        String validRequestJson = """
                {
                    "movieName": "Spiderman",
                    "noOfTickets": 5
                }
                """;

        MockHttpServletRequestBuilder requestBuilder = post("/v1/booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestJson);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

    }

    @Test
    void test_booking_invalid_not_enough_seat() throws Exception {
        when(service.validBookingCount(anyInt())).thenReturn(true);
        when(service.getSelectedMovie(anyString()))
                .thenReturn(
                        Movie.builder()
                                .name("Spiderman")
                                .pricePerSeat(20)
                                .available(3)
                                .taxPercentagePerSeat(15).build()
                );
        when(service.doBooking(any(), any())).thenReturn(true);

        String validRequestJson = """
                {
                    "movieName": "Spiderman",
                    "noOfTickets": 4
                }
                """;

        MockHttpServletRequestBuilder requestBuilder = post("/v1/booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestJson);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Not enough seats are available for Spiderman")));

    }

    @Test
    void test_booking_valid_request() throws Exception {
        when(service.validBookingCount(anyInt())).thenReturn(true);
        when(service.getSelectedMovie(anyString()))
                .thenReturn(
                        Movie.builder()
                                .name("Spiderman")
                                .pricePerSeat(20)
                                .available(5)
                                .taxPercentagePerSeat(15).build()
                );
        when(service.doBooking(any(), any())).thenReturn(true);

        String validRequestJson = """
                {
                    "movieName": "Spiderman",
                    "noOfTickets": 3
                }
                """;

        MockHttpServletRequestBuilder requestBuilder = post("/v1/booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestJson);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("3 seats booked successfully for Spiderman")));

    }


}