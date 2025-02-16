package com.example.gotrip.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter @Builder
public class FlightResponseDTO {
    private String flightCode;
    private String origin;
    private String destination;
    private String airline;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private List<SeatResponseDTO> seats;
}
