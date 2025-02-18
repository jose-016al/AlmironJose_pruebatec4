package com.example.gotrip.controller;

import com.example.gotrip.dto.FlightBookingRequestDTO;
import com.example.gotrip.dto.FlightBookingResponseDTO;
import com.example.gotrip.service.IFlightBookingService;
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
@RequestMapping("/agency/flight-booking")
@RequiredArgsConstructor
@Tag(name = "Reservas de vuelos")
public class FlightBookingController {

    private final IFlightBookingService service;

    @Operation(summary = "Registrar una nueva reserva de vuelo", description = "Crea una nueva reserva para un vuelo específico.")
    @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Error de validación: los datos proporcionados no son válidos")
    @PostMapping("/new")
    public ResponseEntity<FlightBookingResponseDTO> save(@RequestBody @Valid FlightBookingRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @Operation(summary = "Obtener todas las reservas de vuelos", description = "Devuelve la lista de todas las reservas de vuelos existentes.")
    @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida exitosamente")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @GetMapping
    public ResponseEntity<List<FlightBookingResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener una reserva de vuelo por ID", description = "Devuelve los detalles de una reserva específica según su ID.")
    @ApiResponse(responseCode = "200", description = "Reserva encontrada")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @ApiResponse(responseCode = "404", description = "Reserva no encontrada: el ID proporcionado no existe en la base de datos")
    @GetMapping("/{id}")
    public ResponseEntity<FlightBookingResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Obtener reservas por ID de vuelo", description = "Devuelve la lista de reservas asociadas a un vuelo específico.")
    @ApiResponse(responseCode = "200", description = "Reservas obtenidas exitosamente")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @ApiResponse(responseCode = "404", description = "No se encontraron reservas para el vuelo especificado")
    @GetMapping("/flight/{flightId}")
    public ResponseEntity<List<FlightBookingResponseDTO>> findByFlightId(@PathVariable Long flightId) {
        return ResponseEntity.ok(service.findByFlightId(flightId));
    }

    @Operation(summary = "Eliminar una reserva de vuelo", description = "Elimina una reserva de vuelo del sistema según su ID.")
    @ApiResponse(responseCode = "204", description = "Reserva eliminada correctamente")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @ApiResponse(responseCode = "404", description = "Reserva no encontrada: el ID proporcionado no existe en la base de datos")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
