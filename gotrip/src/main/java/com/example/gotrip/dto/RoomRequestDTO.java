package com.example.gotrip.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class RoomRequestDTO {

    @NotNull(message = "El tipo de habitación no puede ser nulo.")
    @Pattern(regexp = "SINGLE|DOUBLE|SUITE", message = "El tipo de habitación debe ser 'SINGLE', 'DOUBLE' o 'SUITE'.")
    private String type;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    private Integer quantity;

    @NotNull(message = "El precio por noche no puede ser nulo")
    @Min(value = 0, message = "El precio por noche no puede ser negativo.")
    private Double pricePerNight;
}
