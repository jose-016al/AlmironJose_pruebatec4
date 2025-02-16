package com.example.gotrip.service;

import com.example.gotrip.exception.FlightException;
import com.example.gotrip.exception.ResourceNotFoundException;
import com.example.gotrip.model.Flight;
import com.example.gotrip.model.Seat;
import com.example.gotrip.model.User;
import com.example.gotrip.util.SeatType;
import com.example.gotrip.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatService implements ISeatService {

    private final SeatRepository repository;
    private final IFlightService flightService;

    @Override
    public Seat findSeatAvailable(String flightCode, SeatType seatType) {
        return findByFlightCode(flightCode).getSeats().stream()
                .filter(seat -> seat.getSeatType().equals(seatType) && seat.isAvailable())
                .findFirst()
                .orElseThrow(() -> new FlightException("No hay asientos disponibles de tipo " + seatType));
    }

    @Override
    public Flight findByFlightCode(String flightCode) {
        return flightService.findByFlightCode(flightCode);
    }

    @Override
    public void reserveSeat(Long id) {
        Seat seat = findSeatOrThrow(id);
        seat.setAvailable(false);
        repository.save(seat);
    }

    @Override
    public void releaseSeat(Long id) {
        Seat seat = findSeatOrThrow(id);
        seat.setAvailable(true);
        repository.save(seat);
    }

    private Seat findSeatOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el asiento con ID: " + id));
    }
}
