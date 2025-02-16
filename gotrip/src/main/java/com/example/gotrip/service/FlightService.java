package com.example.gotrip.service;

import com.example.gotrip.dto.*;
import com.example.gotrip.exception.FlightException;
import com.example.gotrip.exception.HotelException;
import com.example.gotrip.exception.InvalidDateException;
import com.example.gotrip.exception.ResourceNotFoundException;
import com.example.gotrip.model.Flight;
import com.example.gotrip.model.Hotel;
import com.example.gotrip.model.Room;
import com.example.gotrip.model.Seat;
import com.example.gotrip.util.RoomType;
import com.example.gotrip.util.SeatType;
import com.example.gotrip.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class FlightService implements IFlightService {

    private final FlightRepository repository;

    @Override
    public FlightResponseDTO save(FlightRequestDTO request) {
        if (request.getReturnDate() != null && request.getReturnDate().isBefore(request.getDepartureDate())) {
            throw new InvalidDateException("La fecha de regreso debe ser posterior a la fecha de salida.");
        }
        Flight flight = Flight.builder()
                .flightCode(generateFlightCode(request.getOrigin(), request.getDestination()))
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .airline(request.getAirline())
                .departureDate(request.getDepartureDate())
                .returnDate(request.getReturnDate())
                .build();
        flight.setSeats(createSeatsForFlight(request.getSeats(), flight));
        repository.save(flight);
        return buildResponseDTO(flight);
    }

    @Override
    public List<FlightResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::buildResponseDTO)
                .toList();
    }

    @Override
    public List<FlightResponseDTO> searchFlights(LocalDate dateFrom, LocalDate dateTo, String origin, String destination) {
        return repository.searchFlights(dateFrom, dateTo, origin, destination).stream()
                .map(this::buildResponseDTO)
                .toList();
    }

    @Override
    public FlightResponseDTO findById(Long id) {
        return buildResponseDTO(findFlightOrThrow(id));
    }

    @Override
    public Flight findByFlightCode(String flightCode) {
        return repository.findByFlightCode(flightCode)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el vuelo con el codigo: " + flightCode));
    }

    @Override
    public FlightResponseDTO update(Long id, FlightRequestDTO request) {
        Flight flight = findFlightOrThrow(id);

        flight.setOrigin(request.getOrigin());
        flight.setDestination(request.getDestination());
        flight.setAirline(request.getAirline());
        flight.setDepartureDate(request.getDepartureDate());
        flight.setReturnDate(request.getReturnDate());

        repository.save(flight);
        return buildResponseDTO(flight);
    }

    @Override
    public void delete(Long id) {
        Flight flight = findFlightOrThrow(id);
        boolean hasBookings = repository.hasActiveBookings(id);
        if (hasBookings) {
            throw new FlightException("No se puede eliminar el vuelo porque tiene reservas activas.");
        }
        repository.delete(flight);
    }

    private String generateFlightCode(String origin, String destination) {
        String originCode = origin.substring(0, Math.min(origin.length(), 3)).toUpperCase();
        String destinationCode = destination.substring(0, Math.min(destination.length(), 3)).toUpperCase();
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String flightCode = originCode + "-" + destinationCode + "-" + randomNumber;

        return repository.findByFlightCode(flightCode).isPresent() ? generateFlightCode(origin, destination) : flightCode;
    }

    private List<Seat> createSeatsForFlight(List<SeatRequestDTO> seats, Flight flight) {
        final int[] seatNumberCounter = {1};

        return seats.stream()
                .flatMap(seat -> {
                    return IntStream.range(0, seat.getQuantity())
                            .mapToObj(i -> {
                                int seatNumber = seatNumberCounter[0]++;

                                return Seat.builder()
                                        .number(seatNumber)
                                        .seatType(SeatType.valueOf(seat.getType().toUpperCase()))
                                        .available(true)
                                        .price(seat.getPrice())
                                        .flight(flight)
                                        .build();
                            });
                })
                .toList();
    }

    private Flight findFlightOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el vuelo con ID: " + id));
    }

    private FlightResponseDTO buildResponseDTO(Flight flight) {
        return FlightResponseDTO.builder()
                .flightCode(flight.getFlightCode())
                .origin(flight.getOrigin())
                .destination(flight.getDestination())
                .airline(flight.getAirline())
                .departureDate(flight.getDepartureDate())
                .returnDate(flight.getReturnDate())
                .seats(getSeatResponseDTOS(flight))
                .build();
    }

    private List<SeatResponseDTO> getSeatResponseDTOS(Flight flight) {
        return flight.getSeats().stream()
                .collect(Collectors.groupingBy(Seat::getSeatType))
                .entrySet().stream()
                .map(entry -> SeatResponseDTO.builder()
                        .type(entry.getKey().name())
                        .price(entry.getValue().get(0).getPrice())
                        .availableSeats(countAvailableSeatsByType(
                                flight
                        ).getOrDefault(entry.getKey(), 0L).intValue())
                        .build())
                .toList();
    }

    private Map<SeatType, Long> countAvailableSeatsByType(Flight flight) {
        return flight.getSeats().stream()
                .filter(Seat::isAvailable)
                .collect(Collectors.groupingBy(Seat::getSeatType, Collectors.counting()));
    }
}
