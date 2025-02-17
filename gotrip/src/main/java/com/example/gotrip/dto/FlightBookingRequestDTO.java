package com.example.gotrip.dto;

import com.example.gotrip.util.SeatType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class FlightBookingRequestDTO {

    @NotNull(message = "El código de vuelo no puede ser nulo")
    private String flightCode;

    @NotNull(message = "El usuario no puede ser nulo")
    private Long user;

    @NotNull(message = "La lista de pasajeros no puede ser nula.")
    @Size(min = 1, message = "La lista de pasajeros debe contener al menos un elemento.")
    @Valid
    private List<FlightBookingDetailRequestDTO> passengers;
}
