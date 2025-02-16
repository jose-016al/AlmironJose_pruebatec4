package com.example.gotrip.controller;

import com.example.gotrip.dto.HotelRequestDTO;
import com.example.gotrip.dto.HotelResponseDTO;
import com.example.gotrip.service.IHotelService;
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
public class HotelController {

    private final IHotelService service;

    @PostMapping("/new")
    public ResponseEntity<HotelResponseDTO> save(@RequestBody @Valid HotelRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<HotelResponseDTO> update(@PathVariable Long id,
                                                   @RequestBody @Valid HotelRequestDTO request) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
