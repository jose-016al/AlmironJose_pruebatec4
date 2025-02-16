package com.example.gotrip.dto;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class RoomResponseDTO {
    private String type;
    private double pricePerNight;
    private int availableRooms;
}
