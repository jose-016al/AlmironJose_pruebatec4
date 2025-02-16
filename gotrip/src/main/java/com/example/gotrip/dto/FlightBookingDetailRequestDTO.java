package com.example.gotrip.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class FlightBookingDetailRequestDTO {

    @NotBlank(message = "El nombre del pasajero no puede estar vacío")
    private String passengerName;

    @NotBlank(message = "El pasaporte no puede estar vacío")
    @Pattern(regexp = "^[A-Za-z0-9]{6,9}$", message = "El pasaporte debe tener un formato válido (6-9 caracteres alfanuméricos)")
    private String passportNumber;

    @NotNull(message = "El tipo de asiento no puede ser nulo.")
    @Pattern(regexp = "ECONOMY|BUSINESS", message = "El tipo de asiento debe ser 'ECONOMY', 'BUSINESS'.")
    private String seatType;
}
