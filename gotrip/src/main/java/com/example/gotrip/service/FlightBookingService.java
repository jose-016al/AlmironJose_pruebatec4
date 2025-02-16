package com.example.gotrip.service;

import com.example.gotrip.dto.*;
import com.example.gotrip.exception.FlightException;
import com.example.gotrip.exception.InvalidDateException;
import com.example.gotrip.exception.ResourceNotFoundException;
import com.example.gotrip.model.*;
import com.example.gotrip.repository.FlightBookingRepository;
import com.example.gotrip.util.RoomType;
import com.example.gotrip.util.SeatType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightBookingService implements IFlightBookingService {

    private final FlightBookingRepository repository;
    private final IUserService userService;
    private final FlightBookingDetailService flightBookingDetailService;

    @Override
    public FlightBookingResponseDTO save(FlightBookingRequestDTO request) {
        FlightBooking booking = FlightBooking.builder()
                .user(userService.findUser(request.getUser()))
                .build();
        booking.setFlightBookingDetails(flightBookingDetailService.generateDetails(request, booking));
        booking.setTotalPrice(calculateTotalPrice(booking));
        boolean alreadyBooked = repository.existsByUserAndFlight(
                booking.getUser(), flightBookingDetailService.findByFlightCode(request.getFlightCode())
        );
        if (alreadyBooked) {
            throw new FlightException("Ya tienes una reserva en este vuelo.");
        }
        flightBookingDetailService.reserveSeat(booking);
        repository.save(booking);
        return buildResponseDTO(booking);
    }

    @Override
    public List<FlightBookingResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::buildResponseDTO)
                .toList();
    }

    @Override
    public FlightBookingResponseDTO findById(Long id) {
        return buildResponseDTO(findReserveOrThrow(id));
    }

    @Override
    public List<FlightBookingResponseDTO> findByFlightId(Long flightId) {
        return repository.findByFlightId(flightId).stream()
                .map(this::buildResponseDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        FlightBooking booking = findReserveOrThrow(id);
        flightBookingDetailService.releaseSeat(booking);
        repository.delete(booking);
    }

    private FlightBooking findReserveOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado la reserva con el ID: " + id));
    }

    private double calculateTotalPrice(FlightBooking booking) {
        return booking.getFlightBookingDetails().stream()
                .mapToDouble(bookingDetail -> bookingDetail.getSeat().getPrice())
                .sum();
    }

    private Flight findFlight(FlightBooking booking) {
        return booking.getFlightBookingDetails().stream()
                .map(flightBookingDetail -> flightBookingDetail.getSeat().getFlight())
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el vuelo"));
    }


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
