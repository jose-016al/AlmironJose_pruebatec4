package com.example.gotrip.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter @Builder
public class FlightBookingResponseDTO {
    private Long reserveId;
    private String flightCode;
    private LocalDate date;
    private String origin;
    private String destination;
    private String seatType;
    private Integer numberSeat;
    private double price;
    private Long user;
}
