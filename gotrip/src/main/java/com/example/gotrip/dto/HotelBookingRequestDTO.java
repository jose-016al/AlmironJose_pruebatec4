package com.example.gotrip.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class HotelBookingRequestDTO {

    @NotNull(message = "El código de hotel no puede ser nulo")
    private String hotelCode;

    @NotNull(message = "La fecha de entrada no puede ser nula")
    @FutureOrPresent(message = "La fecha de entrada debe ser futura o actual")
    private LocalDate checkIn;

    @FutureOrPresent(message = "La fecha de salida debe ser futura o actual")
    @NotNull(message = "La fecha de salida no puede ser nula")
    private LocalDate checkOut;

    @NotNull(message = "El usuario no puede ser nulo")
    private Long user;

    @NotNull(message = "La lista de huéspedas no puede ser nula.")
    @Size(min = 1, message = "La lista de huéspedas debe contener al menos un elemento.")
    @Valid
    private List<HotelBookingDetailRequestDTO> guests;
}
