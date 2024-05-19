package org.example.booking;

import lombok.extern.slf4j.Slf4j;
import org.example.booking.dto.BookingRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HttpRequestTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    public void testBooking_valid_booking() {
        var url = "http://localhost:" + port + "/v1/booking";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        BookingRequest bookingRequest = BookingRequest.builder().movieName("Spiderman").noOfTickets(3).build();
        HttpEntity<BookingRequest> entity = new HttpEntity<>(bookingRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("3 seats booked successfully for Spiderman");

    }

    @Test
    @Order(2)
    public void testBooking_invalid_request_more_than_available_seats() {
        var url = "http://localhost:" + port + "/v1/booking";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        BookingRequest bookingRequest = BookingRequest.builder().movieName("Spiderman").noOfTickets(4).build();
        HttpEntity<BookingRequest> entity = new HttpEntity<>(bookingRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Not enough seats are available for Spiderman");
    }


    @Test
    @Order(3)
    public void testBooking_invalid_ticket_count() {
        var url = "http://localhost:" + port + "/v1/booking";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        BookingRequest bookingRequest = BookingRequest.builder().movieName("Spiderman").noOfTickets(5).build();
        HttpEntity<BookingRequest> entity = new HttpEntity<>(bookingRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("You can book 1 to 4 tickets maximum");

        BookingRequest bookingRequest2 = BookingRequest.builder().movieName("Spiderman").noOfTickets(0).build();
        HttpEntity<BookingRequest> entity2 = new HttpEntity<>(bookingRequest, headers);
        ResponseEntity<String> response2 = restTemplate.exchange(url, HttpMethod.POST, entity2, String.class);

        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response2.getBody()).isEqualTo("You can book 1 to 4 tickets maximum");

    }

    @Test
    @Order(4)
    public void testBooking_invalid_moviename() {
        var url = "http://localhost:" + port + "/v1/booking";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        BookingRequest bookingRequest = BookingRequest.builder().movieName("X Men").noOfTickets(1).build();
        HttpEntity<BookingRequest> entity = new HttpEntity<>(bookingRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Movie X Men not found");

    }
}


