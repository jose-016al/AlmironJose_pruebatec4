package com.example.gotrip.controller;

import com.example.gotrip.dto.HotelRequestDTO;
import com.example.gotrip.dto.HotelResponseDTO;
import com.example.gotrip.service.IHotelService;
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
@RequestMapping("/agency/hotels")
@RequiredArgsConstructor
@Tag(name = "Hoteles")
public class HotelController {

    private final IHotelService service;

    @Operation(summary = "Crear un nuevo hotel", description = "Registra un nuevo hotel en la base de datos.")
    @ApiResponse(responseCode = "201", description = "Hotel creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Error de validación: los datos proporcionados no son válidos")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @PostMapping("/new")
    public ResponseEntity<HotelResponseDTO> save(@RequestBody @Valid HotelRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @Operation(summary = "Obtener todos los hoteles o buscar por filtros",
            description = "Devuelve la lista de hoteles disponibles. Si se especifican 'dateFrom', 'dateTo' y 'destination', se realiza una búsqueda filtrada.")
    @ApiResponse(responseCode = "200", description = "Lista de hoteles obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<HotelResponseDTO>> getHotels(
            @RequestParam(value = "dateFrom", required = false) String dateFrom,
            @RequestParam(value = "dateTo", required = false) String dateTo,
            @RequestParam(value = "destination", required = false) String destination) {
        if (dateFrom == null || dateTo == null || destination == null) {
            return ResponseEntity.ok(service.findAll());
        }
        return ResponseEntity.ok(service.searchHotels(
                LocalDate.parse(dateFrom),
                LocalDate.parse(dateTo),
                destination
        ));
    }

    @Operation(summary = "Obtener un hotel por ID", description = "Devuelve los detalles de un hotel específico según su ID.")
    @ApiResponse(responseCode = "200", description = "Hotel encontrado")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @ApiResponse(responseCode = "404", description = "Hotel no encontrado: el ID proporcionado no existe en la base de datos")
    @GetMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Actualizar un hotel", description = "Modifica los datos de un hotel según su ID y devuelve la información actualizada.")
    @ApiResponse(responseCode = "200", description = "Hotel actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Error de validación: los datos proporcionados no son válidos")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @ApiResponse(responseCode = "404", description = "Hotel no encontrado: el ID proporcionado no existe en la base de datos")
    @PutMapping("/edit/{id}")
    public ResponseEntity<HotelResponseDTO> update(@PathVariable Long id,
                                                   @RequestParam(required = false) @Valid String name,
                                                   @RequestParam(required = false) @Valid String location,
                                                   @RequestParam(required = false) @Valid Integer stars) {
        return ResponseEntity.ok(service.update(id, name, location, stars));
    }

    @Operation(summary = "Eliminar un hotel", description = "Elimina un hotel de la base de datos según su ID.")
    @ApiResponse(responseCode = "204", description = "Hotel eliminado correctamente")
    @ApiResponse(responseCode = "401", description = "No autorizado. La solicitud requiere autenticación del usuario.")
    @ApiResponse(responseCode = "404", description = "Hotel no encontrado: el ID proporcionado no existe en la base de datos")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
