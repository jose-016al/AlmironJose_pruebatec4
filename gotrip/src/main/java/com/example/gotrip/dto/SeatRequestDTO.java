package com.example.gotrip.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class SeatRequestDTO {

    @NotNull(message = "El tipo de asiento no puede ser nulo.")
    @Pattern(regexp = "ECONOMY|BUSINESS", message = "El tipo de asiento debe ser 'ECONOMY', 'BUSINESS'.")
    private String type;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    private Integer quantity;

    @NotNull(message = "El precio no puede ser nulo")
    @Min(value = 0, message = "El precio no puede ser negativo.")
    private Double price;
}
