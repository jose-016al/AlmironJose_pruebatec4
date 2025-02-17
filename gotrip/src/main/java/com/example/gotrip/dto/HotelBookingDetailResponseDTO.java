package com.example.gotrip.dto;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class HotelBookingDetailResponseDTO {
    private String guestName;
    private String guestIdDocument;
    private String roomType;
    private Integer numberRoom;
}
