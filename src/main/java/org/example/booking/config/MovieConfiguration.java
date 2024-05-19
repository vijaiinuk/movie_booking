package org.example.booking.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.booking.dto.Booking;
import org.example.booking.dto.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class MovieConfiguration {

    @Value("classpath:data.json")
    private Resource data;

    @Value("${booking.ticketcount.min}")
    private int minTicketCount;

    @Value("${booking.ticketcount.max}")
    private int maxTicketCount;


    @Bean
    public Map<String, List<Booking>> bookings() {
        return new HashMap<>();
    }

    @Bean
    public Map<String, Movie> movies() {
        Map<String, Movie> movies = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Movie> movieList = mapper.readValue(data.getFile(), new TypeReference<List<Movie>>() {});
            movieList.forEach(movie -> movies.put(movie.getName(), movie));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("movies: "+movies);
        return movies;
    }

    public int getMinTicketCount() {
        return minTicketCount;
    }

    public int getMaxTicketCount() {
        return maxTicketCount;
    }
}
