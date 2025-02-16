package com.example.gotrip.controller;

import com.example.gotrip.dto.FlightBookingRequestDTO;
import com.example.gotrip.dto.FlightBookingResponseDTO;
import com.example.gotrip.service.IFlightBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agency/flight-booking")
@RequiredArgsConstructor
public class FlightBookingController {

    private final IFlightBookingService service;

    @PostMapping("/new")
    public ResponseEntity<FlightBookingResponseDTO> save(@RequestBody @Valid FlightBookingRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @GetMapping
    public ResponseEntity<List<FlightBookingResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightBookingResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<List<FlightBookingResponseDTO>> findByFlightId(@PathVariable Long flightId) {
        return ResponseEntity.ok(service.findByFlightId(flightId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
