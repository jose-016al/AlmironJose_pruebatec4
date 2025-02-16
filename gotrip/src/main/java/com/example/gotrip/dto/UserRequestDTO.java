package com.example.gotrip.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class UserRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String lastName;

    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "El correo electrónico debe tener un formato válido")
    private String email;

    @NotBlank(message = "El pasaporte no puede estar vacío")
    @Pattern(regexp = "^[A-Za-z0-9]{6,9}$", message = "El pasaporte debe tener un formato válido (6-9 caracteres alfanuméricos)")
    private String passport;
}
