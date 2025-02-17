package com.example.gotrip.service;

import com.example.gotrip.dto.*;
import com.example.gotrip.exception.FlightException;
import com.example.gotrip.exception.InvalidDateException;
import com.example.gotrip.exception.ResourceNotFoundException;
import com.example.gotrip.model.Flight;
import com.example.gotrip.model.Seat;
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

    /**
     * Guarda una nueva reserva de vuelo.
     * @param request Datos de la solicitud de la reserva
     * @return DTO de la respuesta con los detalles de la reserva
     */
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

    /**
     * Obtiene todos los vuelos registrados.
     * @return Lista de DTOs de vuelos con sus detalles
     */
    @Override
    public List<FlightResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::buildResponseDTO)
                .toList();
    }

    /**
     * Busca vuelos según las fechas y los lugares de origen y destino.
     * @param dateFrom Fecha de inicio de la búsqueda
     * @param dateTo Fecha de fin de la búsqueda
     * @param origin Ciudad de origen
     * @param destination Ciudad de destino
     * @return Lista de DTOs de vuelos que coinciden con la búsqueda
     */
    @Override
    public List<FlightResponseDTO> searchFlights(LocalDate dateFrom, LocalDate dateTo, String origin, String destination) {
        return repository.searchFlights(dateFrom, dateTo, origin, destination).stream()
                .map(this::buildResponseDTO)
                .toList();
    }

    /**
     * Encuentra un vuelo por su ID.
     * @param id ID del vuelo
     * @return DTO de la respuesta con los detalles del vuelo
     */
    @Override
    public FlightResponseDTO findById(Long id) {
        return buildResponseDTO(findFlightOrThrow(id));
    }

    /**
     * Encuentra un vuelo por su código de vuelo.
     * @param flightCode Código único del vuelo
     * @return Objeto vuelo correspondiente al código proporcionado
     * @throws ResourceNotFoundException Si no se encuentra el vuelo
     */
    @Override
    public Flight findByFlightCode(String flightCode) {
        return repository.findByFlightCode(flightCode)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el vuelo con el codigo: " + flightCode));
    }

    /**
     * Actualiza un vuelo existente con nuevos datos.
     * @param id ID del vuelo a actualizar
     * @param request Nuevos datos de la solicitud
     * @return DTO de la respuesta con los detalles del vuelo actualizado
     */
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

    /**
     * Elimina un vuelo de la base de datos.
     * @param id ID del vuelo a eliminar
     * @throws FlightException Si el vuelo tiene reservas activas
     */
    @Override
    public void delete(Long id) {
        Flight flight = findFlightOrThrow(id);
        boolean hasBookings = repository.hasActiveBookings(id);
        if (hasBookings) {
            throw new FlightException("No se puede eliminar el vuelo porque tiene reservas activas.");
        }
        repository.delete(flight);
    }

    /**
     * Genera un código único para el vuelo basado en el origen, destino y un número aleatorio.
     * @param origin Ciudad de origen
     * @param destination Ciudad de destino
     * @return Código único para el vuelo
     */
    private String generateFlightCode(String origin, String destination) {
        String originCode = origin.substring(0, Math.min(origin.length(), 3)).toUpperCase();
        String destinationCode = destination.substring(0, Math.min(destination.length(), 3)).toUpperCase();
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String flightCode = originCode + "-" + destinationCode + "-" + randomNumber;

        return repository.findByFlightCode(flightCode).isPresent() ? generateFlightCode(origin, destination) : flightCode;
    }

    /**
     * Crea los asientos asociados a un vuelo.
     * @param seats Datos de los asientos a crear
     * @param flight Vuelo al que pertenecen los asientos
     * @return Lista de asientos creados
     */
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

    /**
     * Busca un vuelo por su ID y lanza una excepción si no se encuentra.
     * @param id ID del vuelo
     * @return Vuelo correspondiente al ID
     * @throws ResourceNotFoundException Si no se encuentra el vuelo
     */
    private Flight findFlightOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el vuelo con ID: " + id));
    }

    /**
     * Construye un DTO de respuesta para un vuelo.
     * @param flight Vuelo a convertir en DTO
     * @return DTO con los detalles del vuelo
     */
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

    /**
     * Obtiene los DTOs de los asientos de un vuelo.
     * @param flight Vuelo cuyos asientos se quieren obtener
     * @return Lista de DTOs de los asientos
     */
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

    /**
     * Cuenta el número de asientos disponibles por tipo en un vuelo.
     * @param flight Vuelo cuyo número de asientos disponibles se quiere contar
     * @return Mapa de tipos de asiento y el número de asientos disponibles de ese tipo
     */
    private Map<SeatType, Long> countAvailableSeatsByType(Flight flight) {
        return flight.getSeats().stream()
                .filter(Seat::isAvailable)
                .collect(Collectors.groupingBy(Seat::getSeatType, Collectors.counting()));
    }
}
