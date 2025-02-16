package com.example.gotrip.controller;

import com.example.gotrip.dto.FlightRequestDTO;
import com.example.gotrip.dto.FlightResponseDTO;
import com.example.gotrip.dto.UserRequestDTO;
import com.example.gotrip.dto.UserResponseDTO;
import com.example.gotrip.service.IFlightService;
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
public class FlightController {

    private final IFlightService service;

    @PostMapping("/new")
    public ResponseEntity<FlightResponseDTO> save(@RequestBody @Valid FlightRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(request));
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<FlightResponseDTO> update(@PathVariable Long id,
                                                  @RequestBody @Valid FlightRequestDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
