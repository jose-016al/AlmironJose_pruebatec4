package com.example.gotrip.controller;

import com.example.gotrip.dto.HotelBookingRequestDTO;
import com.example.gotrip.dto.HotelBookingResponseDTO;
import com.example.gotrip.service.IHotelBookingService;
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
@RequestMapping("/agency/room-booking")
@RequiredArgsConstructor
@Tag(name = "Reservas de hotel")
public class HotelBookingController {

    private final IHotelBookingService service;

    @Operation(summary = "Crear una nueva reserva de hotel", description = "Registra una nueva reserva de habitación en un hotel.")
    @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Error de validación: los datos proporcionados no son válidos")
    @PostMapping("/new")
    public ResponseEntity<HotelBookingResponseDTO> save(@RequestBody @Valid HotelBookingRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @Operation(summary = "Obtener todas las reservas", description = "Devuelve la lista de todas las reservas de hotel.")
    @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida exitosamente")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @GetMapping
    public ResponseEntity<List<HotelBookingResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener una reserva por ID", description = "Devuelve los detalles de una reserva específica según su ID.")
    @ApiResponse(responseCode = "200", description = "Reserva encontrada")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @ApiResponse(responseCode = "404", description = "Reserva no encontrada: el ID proporcionado no existe en la base de datos")
    @GetMapping("/{id}")
    public ResponseEntity<HotelBookingResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Obtener reservas por ID de hotel",
            description = "Devuelve la lista de reservas asociadas a un hotel específico según su ID.")
    @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida exitosamente")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @ApiResponse(responseCode = "404", description = "Hotel no encontrado: el ID proporcionado no existe en la base de datos")
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<HotelBookingResponseDTO>> findByHotelId(@PathVariable Long hotelId) {
        return ResponseEntity.ok(service.findByHotelId(hotelId));
    }

    @Operation(summary = "Eliminar una reserva de hotel", description = "Elimina una reserva de hotel según su ID.")
    @ApiResponse(responseCode = "204", description = "Reserva eliminada correctamente")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @ApiResponse(responseCode = "404", description = "Reserva no encontrada: el ID proporcionado no existe en la base de datos")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
