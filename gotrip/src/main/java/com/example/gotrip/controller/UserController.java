package com.example.gotrip.controller;

import com.example.gotrip.dto.UserRequestDTO;
import com.example.gotrip.dto.UserResponseDTO;
import com.example.gotrip.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agency/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios")
public class UserController {

    private final IUserService service;

    @Operation(summary = "Crear un nuevo usuario", description = "Guarda un usuario en la base de datos y devuelve los detalles del usuario creado.")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Error de validación: los datos proporcionados no son válidos")
    @PostMapping("/new")
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Valid UserRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(request));
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista con todos los usuarios registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener un usuario por ID", description = "Devuelve los detalles de un usuario específico según su ID.")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado: el ID proporcionado no existe en la base de datos")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Actualizar un usuario", description = "Modifica los datos de un usuario según su ID y devuelve la información actualizada.")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Error de validación: los datos proporcionados no son válidos")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado: el ID proporcionado no existe en la base de datos")
    @PutMapping("/edit/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id,
                                                  @RequestBody @Valid UserRequestDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario de la base de datos según su ID.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado: el ID proporcionado no existe en la base de datos")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
