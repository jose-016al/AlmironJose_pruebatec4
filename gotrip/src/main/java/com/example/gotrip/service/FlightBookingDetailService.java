package com.example.gotrip.service;

import com.example.gotrip.dto.FlightBookingDetailRequestDTO;
import com.example.gotrip.dto.FlightBookingDetailResponseDTO;
import com.example.gotrip.dto.FlightBookingRequestDTO;
import com.example.gotrip.model.*;
import com.example.gotrip.repository.FlightBookingDetailRepository;
import com.example.gotrip.util.SeatType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightBookingDetailService implements IFlightBookingDetailService {

    private final FlightBookingDetailRepository repository;
    private final ISeatService seatService;

    /**
     * Método para generar los detalles de la reserva de un vuelo.
     * @param request Datos de la solicitud de la reserva
     * @param booking Reserva de vuelo
     * @return Lista de detalles de la reserva de vuelo
     */
    @Override
    public List<FlightBookingDetail> generateDetails(FlightBookingRequestDTO request, FlightBooking booking) {
        Map<SeatType, List<Seat>> availableSeatsByType = findSeatAvailable(
                request.getFlightCode(),
                request.getPassengers()
        );
        seatService.reserveSeat(availableSeatsByType.values()
                .stream()
                .flatMap(List::stream)
                .toList());
        Map<SeatType, AtomicInteger> seatIndexByType = availableSeatsByType.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> new AtomicInteger(0)));

        return request.getPassengers().stream()
                .map(passenger -> {
                    SeatType type = SeatType.valueOf(passenger.getSeatType().toUpperCase());
                    List<Seat> seats = availableSeatsByType.getOrDefault(type, List.of());
                    int index = seatIndexByType.get(type).getAndIncrement();
                    Seat assignedSeat = index < seats.size() ? seats.get(index) : null;

                    return FlightBookingDetail.builder()
                            .passengerName(passenger.getPassengerName())
                            .passportNumber(passenger.getPassportNumber())
                            .seat(assignedSeat)
                            .flightBooking(booking)
                            .build();
                })
                .toList();
    }

    /**
     * Método para encontrar un vuelo por su código.
     * @param flightCode Código del vuelo
     * @return Vuelo correspondiente al código
     */
    @Override
    public Flight findByFlightCode(String flightCode) {
        return seatService.findByFlightCode(flightCode);
    }

    /**
     * Método para liberar los asientos de una reserva de vuelo.
     * @param booking Reserva de vuelo a la que se le liberarán los asientos
     */
    @Override
    public void releaseSeat(FlightBooking booking) {
        seatService.releaseSeat(booking.getFlightBookingDetails().stream()
                .map(FlightBookingDetail::getSeat)
                .toList());
    }

    /**
     * Método para obtener los detalles de la reserva de vuelo en formato DTO.
     * @param booking Reserva de vuelo
     * @return Lista de DTOs con los detalles de la reserva de vuelo
     */
    @Override
    public List<FlightBookingDetailResponseDTO> getDetailsResponseDTOS(FlightBooking booking) {
        return booking.getFlightBookingDetails().stream()
                .map(bookingDetail -> FlightBookingDetailResponseDTO.builder()
                        .passengerName(bookingDetail.getPassengerName())
                        .passportNumber(bookingDetail.getPassportNumber())
                        .numberSeat(bookingDetail.getSeat().getNumber())
                        .seatType(bookingDetail.getSeat().getSeatType().name())
                        .build()
                )
                .toList();
    }

    /**
     * Método privado para encontrar los asientos disponibles por tipo de asiento.
     * @param flightCode Código del vuelo
     * @param passengers Lista de pasajeros con su tipo de asiento
     * @return Mapa con los tipos de asientos y la lista de asientos disponibles para cada tipo
     */
    private Map<SeatType, List<Seat>> findSeatAvailable(String flightCode,
                                                        List<FlightBookingDetailRequestDTO> passengers) {
        return passengers.stream()
                .map(passenger -> SeatType.valueOf(passenger.getSeatType().toUpperCase()))
                .distinct()
                .collect(Collectors.toMap(
                        type -> type,
                        type -> seatService.findSeatsAvailable(flightCode, type,
                                (int) passengers.stream().filter(p -> p.getSeatType().equalsIgnoreCase(type.name())).count())
                ));
    }

}
