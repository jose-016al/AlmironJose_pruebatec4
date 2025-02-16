package com.example.gotrip.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class HotelResponseDTO {
    private String hotelCode;
    private String name;
    private String location;
    private Integer stars;
    private List<RoomResponseDTO> rooms;
}
