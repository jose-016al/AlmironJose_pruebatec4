package com.example.gotrip.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;

@Getter
public class HotelRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    private String name;

    @NotBlank(message = "La ubicación no puede estar vacía")
    @Size(max = 150, message = "La ubicación no debe superar los 150 caracteres")
    private String location;

    @Min(value = 1, message = "El hotel debe tener al menos 1 estrella")
    @Max(value = 5, message = "El hotel no puede tener más de 5 estrellas")
    @NotNull(message = "El número de estrellas no puede ser nulo.")
    private Integer stars;

    @NotNull(message = "La lista de habitaciones no puede ser nula.")
    @Valid
    private List<RoomRequestDTO> rooms;
}
