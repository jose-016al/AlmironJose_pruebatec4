package com.example.gotrip.controller;

import com.example.gotrip.dto.HotelBookingRequestDTO;
import com.example.gotrip.dto.HotelBookingResponseDTO;
import com.example.gotrip.service.IHotelBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agency/room-booking")
@RequiredArgsConstructor
public class HotelBookingController {

    private final IHotelBookingService service;

    @PostMapping("/new")
    public ResponseEntity<HotelBookingResponseDTO> save(@RequestBody @Valid HotelBookingRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @GetMapping
    public ResponseEntity<List<HotelBookingResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelBookingResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<HotelBookingResponseDTO>> findByHotelId(@PathVariable Long hotelId) {
        return ResponseEntity.ok(service.findByHotelId(hotelId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
