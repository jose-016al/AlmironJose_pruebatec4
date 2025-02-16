package com.example.gotrip.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter @Builder
public class FlightBookingResponseDTO {
    private Long reserveId;
    private String flightCode;
    private LocalDate date;
    private String origin;
    private String destination;
    private double totalPrice;
    private Long user;
    private List<FlightBookingDetailResponseDTO> passengers;
}
