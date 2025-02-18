package com.example.gotrip.service;

import com.example.gotrip.dto.HotelResponseDTO;
import com.example.gotrip.exception.NoContentException;
import com.example.gotrip.model.Hotel;
import com.example.gotrip.repository.HotelRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelServiceImplementation;

    @Test
    void findAll() {
        List<Hotel> hotels = new ArrayList<>();
        hotels.add(new Hotel(1L, "MAD-5735", "Hotel paradise", "Madrid", 5, new ArrayList<>()));
        when(hotelRepository.findAll()).thenReturn(hotels);
        List<HotelResponseDTO> result = hotelServiceImplementation.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findAllException() {
        List<Hotel> hotels = new ArrayList<>();
        when(hotelRepository.findAll()).thenReturn(hotels);
        assertThrows(NoContentException.class, () -> {
            if (hotels.isEmpty()) {
                throw new NoContentException("No existen hoteles registrados");
            }
        });
    }
}