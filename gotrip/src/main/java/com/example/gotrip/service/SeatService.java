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

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService implements ISeatService {

    private final SeatRepository repository;
    private final IFlightService flightService;

    @Override
    public List<Seat> findSeatsAvailable(String flightCode, SeatType seatType, int numberOfSeats) {
        List<Seat> availableSeats = repository.findAvailableSeats(flightCode, seatType, numberOfSeats);

        if (availableSeats.size() < numberOfSeats) {
            throw new FlightException("No hay suficientes asientos disponibles de tipo " + seatType);
        }

        return availableSeats;
    }

    @Override
    public Flight findByFlightCode(String flightCode) {
        return flightService.findByFlightCode(flightCode);
    }

    @Override
    public void reserveSeat(List<Seat> seats) {
        seats.forEach(seat -> seat.setAvailable(false));
        repository.saveAll(seats);
    }

    @Override
    public void releaseSeat(List<Seat> seats) {
        seats.forEach(seat -> seat.setAvailable(true));
        repository.saveAll(seats);
    }
}
