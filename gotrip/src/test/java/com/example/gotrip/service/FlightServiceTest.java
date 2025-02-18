package com.example.gotrip.service;

import com.example.gotrip.dto.FlightResponseDTO;
import com.example.gotrip.dto.HotelResponseDTO;
import com.example.gotrip.exception.NoContentException;
import com.example.gotrip.model.Flight;
import com.example.gotrip.model.Hotel;
import com.example.gotrip.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepositoryRepository;

    @InjectMocks
    private FlightService flightServiceImplementation;

    @Test
    void findAll() {
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight(1L, "MAD-NEW-5735", "Madrid", "Barcelona", "Iberia", LocalDate.of(20255,03,20), LocalDate.of(20255,03,25), new ArrayList<>()));
        when(flightRepositoryRepository.findAll()).thenReturn(flights);
        List<FlightResponseDTO> result = flightServiceImplementation.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findAllException() {
        List<Flight> flights = new ArrayList<>();
        when(flightRepositoryRepository.findAll()).thenReturn(flights);
        assertThrows(NoContentException.class, () -> {
            if (flights.isEmpty()) {
                throw new NoContentException("No existen hoteles registrados");
            }
        });
    }
}
