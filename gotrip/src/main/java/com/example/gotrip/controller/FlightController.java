package com.example.gotrip.controller;

import com.example.gotrip.dto.FlightRequestDTO;
import com.example.gotrip.dto.FlightResponseDTO;
import com.example.gotrip.dto.UserRequestDTO;
import com.example.gotrip.dto.UserResponseDTO;
import com.example.gotrip.service.IFlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agency/flights")
@RequiredArgsConstructor
@Tag(name = "Vuelos")
public class FlightController {

    private final IFlightService service;

    @Operation(summary = "Registrar un nuevo vuelo", description = "Crea un nuevo vuelo en el sistema.")
    @ApiResponse(responseCode = "201", description = "Vuelo registrado exitosamente")
    @ApiResponse(responseCode = "400", description = "Error de validación: los datos proporcionados no son válidos")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @PostMapping("/new")
    public ResponseEntity<FlightResponseDTO> save(@RequestBody @Valid FlightRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(request));
    }

    @Operation(summary = "Obtener todos los vuelos o buscar por filtros",
            description = "Devuelve la lista de hoteles disponibles. Si se especifican 'dateFrom', 'dateTo', 'origin' y 'destination', se realiza una búsqueda filtrada.")
    @ApiResponse(responseCode = "200", description = "Lista de vuelos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<FlightResponseDTO>> getFlights(
            @RequestParam(value = "dateFrom", required = false) String dateFrom,
            @RequestParam(value = "dateTo", required = false) String dateTo,
            @RequestParam(value = "origin", required = false) String origin,
            @RequestParam(value = "destination", required = false) String destination) {

        if (dateFrom == null || dateTo == null || origin == null || destination == null) {
            return ResponseEntity.ok(service.findAll());
        }

        return ResponseEntity.ok(service.searchFlights(
                LocalDate.parse(dateFrom),
                LocalDate.parse(dateTo),
                origin,
                destination
        ));
    }

    @Operation(summary = "Obtener un vuelo por ID", description = "Devuelve los detalles de un vuelo específico según su ID.")
    @ApiResponse(responseCode = "200", description = "Vuelo encontrado")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @ApiResponse(responseCode = "404", description = "Vuelo no encontrado: el ID proporcionado no existe en la base de datos")
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Actualizar un vuelo", description = "Modifica los datos de un vuelo existente.")
    @ApiResponse(responseCode = "200", description = "Vuelo actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Error de validación: los datos proporcionados no son válidos")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @ApiResponse(responseCode = "404", description = "Vuelo no encontrado: el ID proporcionado no existe en la base de datos")
    @PutMapping("/edit/{id}")
    public ResponseEntity<FlightResponseDTO> update(@PathVariable Long id,
                                                    @RequestParam(required = false) @Valid String origin,
                                                    @RequestParam(required = false) @Valid String destination,
                                                    @RequestParam(required = false) @Valid String airline,
                                                    @RequestParam(required = false) @Valid LocalDate departureDate,
                                                    @RequestParam(required = false) @Valid LocalDate returnDate) {
        return ResponseEntity.ok(service.update(id, origin, destination, airline, departureDate, returnDate));
    }

    @Operation(summary = "Eliminar un vuelo", description = "Elimina un vuelo del sistema según su ID.")
    @ApiResponse(responseCode = "204", description = "Vuelo eliminado correctamente")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @ApiResponse(responseCode = "404", description = "Vuelo no encontrado: el ID proporcionado no existe en la base de datos")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
