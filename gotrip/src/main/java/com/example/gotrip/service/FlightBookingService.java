package com.example.gotrip.service;

import com.example.gotrip.dto.*;
import com.example.gotrip.exception.FlightException;
import com.example.gotrip.exception.NoContentException;
import com.example.gotrip.exception.ResourceNotFoundException;
import com.example.gotrip.model.*;
import com.example.gotrip.repository.FlightBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightBookingService implements IFlightBookingService {

    private final FlightBookingRepository repository;
    private final IUserService userService;
    private final FlightBookingDetailService flightBookingDetailService;

    /**
     * Guarda una nueva reserva de vuelo.
     * @param request Datos de la solicitud de la reserva
     * @return DTO de la respuesta con los detalles de la reserva
     */
    @Override
    public FlightBookingResponseDTO save(FlightBookingRequestDTO request) {
        boolean alreadyBooked = repository.existsByUserAndFlight(
                userService.findUser(request.getUser()),
                flightBookingDetailService.findByFlightCode(request.getFlightCode())
        );
        if (alreadyBooked) {
            throw new FlightException("Ya tienes una reserva en este vuelo.");
        }
        FlightBooking booking = FlightBooking.builder()
                .user(userService.findUser(request.getUser()))
                .build();
        booking.setFlightBookingDetails(flightBookingDetailService.generateDetails(request, booking));
        booking.setTotalPrice(calculateTotalPrice(booking));
        repository.save(booking);
        return buildResponseDTO(booking);
    }

    /**
     * Obtiene todas las reservas de vuelo.
     * @return Lista de todas las reservas en formato DTO
     */
    @Override
    public List<FlightBookingResponseDTO> findAll() {
        List<FlightBooking> bookings= repository.findAll();
        if (bookings.isEmpty()) {
            throw new NoContentException("No hay reservas disponibles en este momento.");
        }
        return bookings.stream()
                .map(this::buildResponseDTO)
                .toList();
    }

    /**
     * Encuentra una reserva por su ID.
     * @param id ID de la reserva
     * @return DTO con los detalles de la reserva
     */
    @Override
    public FlightBookingResponseDTO findById(Long id) {
        return buildResponseDTO(findReserveOrThrow(id));
    }

    /**
     * Encuentra las reservas relacionadas con un vuelo específico.
     * @param flightId ID del vuelo
     * @return Lista de reservas para el vuelo
     */
    @Override
    public List<FlightBookingResponseDTO> findByFlightId(Long flightId) {
        List<FlightBooking> bookings = repository.findByFlightId(flightId) ;
        if (bookings.isEmpty()) {
            throw new NoContentException("No hay reservas disponibles en este momento.");
        }
        return bookings.stream()
                .map(this::buildResponseDTO)
                .toList();
    }

    /**
     * Elimina una reserva por su ID.
     * @param id ID de la reserva a eliminar
     */
    @Override
    public void delete(Long id) {
        FlightBooking booking = findReserveOrThrow(id);
        flightBookingDetailService.releaseSeat(booking);
        repository.delete(booking);
    }

    /**
     * Encuentra una reserva por su ID o lanza una excepción si no se encuentra.
     * @param id ID de la reserva
     * @return La reserva encontrada
     * @throws ResourceNotFoundException Si no se encuentra la reserva
     */
    private FlightBooking findReserveOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado la reserva con el ID: " + id));
    }

    /**
     * Calcula el precio total de una reserva sumando los precios de los asientos.
     * @param booking Reserva de vuelo
     * @return Precio total de la reserva
     */
    private double calculateTotalPrice(FlightBooking booking) {
        return booking.getFlightBookingDetails().stream()
                .mapToDouble(bookingDetail -> bookingDetail.getSeat().getPrice())
                .sum();
    }

    /**
     * Encuentra el vuelo relacionado con una reserva.
     * @param booking Reserva de vuelo
     * @return Vuelo relacionado con la reserva
     * @throws ResourceNotFoundException Si no se encuentra el vuelo
     */
    private Flight findFlight(FlightBooking booking) {
        return booking.getFlightBookingDetails().stream()
                .map(flightBookingDetail -> flightBookingDetail.getSeat().getFlight())
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el vuelo"));
    }

    /**
     * Construye un DTO con los detalles de la reserva de vuelo.
     * @param booking Reserva de vuelo
     * @return DTO con los detalles de la reserva
     */
    private FlightBookingResponseDTO buildResponseDTO(FlightBooking booking) {
        return FlightBookingResponseDTO.builder()
                .reserveId(booking.getId())
                .date(findFlight(booking).getDepartureDate())
                .origin(findFlight(booking).getOrigin())
                .destination(findFlight(booking).getDestination())
                .flightCode(findFlight(booking).getFlightCode())
                .totalPrice(booking.getTotalPrice())
                .passengers(flightBookingDetailService.getDetailsResponseDTOS(booking))
                .user(booking.getUser().getId())
                .build();
    }
}
