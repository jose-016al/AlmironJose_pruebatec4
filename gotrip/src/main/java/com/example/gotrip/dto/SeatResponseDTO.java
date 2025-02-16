package com.example.gotrip.dto;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class SeatResponseDTO {
    private String type;
    private double price;
    private int availableSeats;
}
