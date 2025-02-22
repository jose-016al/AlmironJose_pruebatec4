package com.example.gotrip.dto;

import com.example.gotrip.model.Room;
import com.example.gotrip.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter @Builder
public class HotelBookingResponseDTO {
    private Long reserveId;
    private String hotelCode;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Double totalPrice;
    private Long user;
    private List<HotelBookingDetailResponseDTO> guests;
}
