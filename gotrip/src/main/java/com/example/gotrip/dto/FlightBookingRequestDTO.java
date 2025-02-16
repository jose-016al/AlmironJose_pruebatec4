package com.example.gotrip.dto;

import com.example.gotrip.util.SeatType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class FlightBookingRequestDTO {

    @NotNull(message = "El código de vuelo no puede ser nulo")
    private String flightCode;

    @NotNull(message = "El tipo de asiento no puede ser nulo.")
    @Pattern(regexp = "ECONOMY|BUSINESS", message = "El tipo de asiento debe ser 'ECONOMY', 'BUSINESS'.")
    private String seatType;

    @NotNull(message = "El usuario no puede ser nulo")
    private Long user;
}
