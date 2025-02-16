package com.example.gotrip.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter @Builder
public class FlightRequestDTO {

    @NotBlank(message = "El origen no puede estar vacío")
    private String origin;

    @NotBlank(message = "El destino no puede estar vacío")
    private String destination;

    @NotBlank(message = "La aerolínea no puede estar vacía")
    private String airline;

    @NotNull(message = "La fecha de salida no puede ser nula")
    @Future(message = "La fecha de salida debe ser en el futuro")
    private LocalDate departureDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate returnDate;

    @NotNull(message = "La lista de asientos no puede ser nula.")
    @Valid
    private List<SeatRequestDTO> seats;
}
