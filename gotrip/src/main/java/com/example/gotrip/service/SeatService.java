package com.example.gotrip.service;

import com.example.gotrip.exception.FlightException;
import com.example.gotrip.model.Flight;
import com.example.gotrip.model.Seat;
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

    /**
     * Método para encontrar asientos disponibles en un vuelo específico.
     *
     * @param flightCode El código del vuelo donde buscar los asientos.
     * @param seatType El tipo de asiento que se busca.
     * @param numberOfSeats El número de asientos que se desean reservar.
     * @return Lista de asientos disponibles.
     * @throws FlightException Si no hay suficientes asientos disponibles del tipo solicitado.
     */
    @Override
    public List<Seat> findSeatsAvailable(String flightCode, SeatType seatType, int numberOfSeats) {
        List<Seat> availableSeats = repository.findAvailableSeats(flightCode, seatType, numberOfSeats);

        if (availableSeats.size() < numberOfSeats) {
            throw new FlightException("No hay suficientes asientos disponibles de tipo " + seatType);
        }

        return availableSeats;
    }

    /**
     * Método para obtener un vuelo a partir de su código.
     *
     * @param flightCode El código del vuelo.
     * @return El objeto Flight correspondiente al código proporcionado.
     */
    @Override
    public Flight findByFlightCode(String flightCode) {
        return flightService.findByFlightCode(flightCode);
    }

    /**
     * Método para reservar asientos en un vuelo.
     *
     * @param seats Lista de asientos a reservar.
     */
    @Override
    public void reserveSeat(List<Seat> seats) {
        seats.forEach(seat -> seat.setAvailable(false));
        repository.saveAll(seats);
    }

    /**
     * Método para liberar asientos en un vuelo.
     *
     * @param seats Lista de asientos a liberar.
     */
    @Override
    public void releaseSeat(List<Seat> seats) {
        seats.forEach(seat -> seat.setAvailable(true));
        repository.saveAll(seats);
    }
}
