package com.example.gotrip.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.time.LocalDate;

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

    @NotNull(message = "El tipo de habitación no puede ser nulo.")
    @Pattern(regexp = "SINGLE|DOUBLE|SUITE", message = "El tipo de habitación debe ser 'SINGLE', 'DOUBLE' o 'SUITE'.")
    private String roomType;

    @NotNull(message = "El usuario no puede ser nulo")
    private Long user;
}
