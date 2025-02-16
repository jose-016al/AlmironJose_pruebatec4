package com.example.gotrip.dto;


import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class FlightBookingDetailResponseDTO {
    private String passengerName;
    private String passportNumber;
    private Integer numberSeat;
    private String seatType;
}
