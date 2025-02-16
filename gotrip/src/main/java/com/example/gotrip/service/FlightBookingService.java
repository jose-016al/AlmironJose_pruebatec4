package com.example.gotrip.service;

import com.example.gotrip.dto.FlightBookingRequestDTO;
import com.example.gotrip.dto.FlightBookingResponseDTO;
import com.example.gotrip.dto.HotelBookingRequestDTO;
import com.example.gotrip.dto.HotelBookingResponseDTO;
import com.example.gotrip.exception.FlightException;
import com.example.gotrip.exception.InvalidDateException;
import com.example.gotrip.exception.ResourceNotFoundException;
import com.example.gotrip.model.FlightBooking;
import com.example.gotrip.model.HotelBooking;
import com.example.gotrip.model.Room;
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
    private final ISeatService seatService;

    @Override
    public FlightBookingResponseDTO save(FlightBookingRequestDTO request) {
        FlightBooking booking = FlightBooking.builder()
                .seat(seatService.findSeatAvailable(
                        request.getFlightCode(),
                        SeatType.valueOf(request.getSeatType().toUpperCase())
                ))
                .user(userService.findUser(request.getUser()))
                .build();
        boolean alreadyBooked = repository.existsByUserAndFlight(
                booking.getUser(), booking.getSeat().getFlight()
        );
        if (alreadyBooked) {
            throw new FlightException("Ya tienes una reserva en este vuelo.");
        }
        seatService.reserveSeat(booking.getSeat().getId());
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
        seatService.releaseSeat(id);
        repository.delete(booking);
    }

    private FlightBooking findReserveOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado la reserva con el ID: " + id));
    }

    private FlightBookingResponseDTO buildResponseDTO(FlightBooking booking) {
        return FlightBookingResponseDTO.builder()
                .reserveId(booking.getId())
                .date(booking.getSeat().getFlight().getDepartureDate())
                .origin(booking.getSeat().getFlight().getOrigin())
                .destination(booking.getSeat().getFlight().getDestination())
                .flightCode(booking.getSeat().getFlight().getFlightCode())
                .seatType(booking.getSeat().getSeatType().name())
                .numberSeat(booking.getSeat().getNumber())
                .price(booking.getSeat().getPrice())
                .user(booking.getUser().getId())
                .build();
    }
}
