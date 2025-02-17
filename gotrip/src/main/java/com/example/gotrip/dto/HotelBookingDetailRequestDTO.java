package com.example.gotrip.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class HotelBookingDetailRequestDTO {

    @NotBlank(message = "El nombre del huésped no puede estar vacío")
    private String guestName;

    @NotBlank(message = "El DNI no puede estar vacío")
    @Pattern(regexp = "^[A-Za-z0-9]{6,9}$", message = "El dni debe tener un formato válido (6-9 caracteres alfanuméricos)")
    private String guestIdDocument;

    @NotNull(message = "El tipo de habitación no puede ser nulo.")
    @Pattern(regexp = "SINGLE|DOUBLE|SUITE", message = "El tipo de habitación debe ser 'SINGLE', 'DOUBLE' o 'SUITE'.")
    private String roomType;
}
