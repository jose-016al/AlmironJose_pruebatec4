package com.example.gotrip.service;

import com.example.gotrip.dto.FlightBookingDetailRequestDTO;
import com.example.gotrip.dto.FlightBookingDetailResponseDTO;
import com.example.gotrip.dto.FlightBookingRequestDTO;
import com.example.gotrip.model.Flight;
import com.example.gotrip.model.FlightBooking;
import com.example.gotrip.model.FlightBookingDetail;
import com.example.gotrip.model.Seat;
import com.example.gotrip.repository.FlightBookingDetailRepository;
import com.example.gotrip.util.SeatType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class FlightBookingDetailService implements IFlightBookingDetailService {

    private final FlightBookingDetailRepository repository;
    private final ISeatService seatService;

    @Override
    public List<FlightBookingDetail> generateDetails(FlightBookingRequestDTO request, FlightBooking booking) {
        List<Seat> seats = findSeatAvailable(request.getFlightCode(), request.getPassengers());
        AtomicInteger index = new AtomicInteger(0);
        return request.getPassengers().stream()
                .map(detail -> FlightBookingDetail.builder()
                        .passengerName(detail.getPassengerName())
                        .passportNumber(detail.getPassportNumber())
                        .seat(seats.get(index.getAndIncrement()))
                        .flightBooking(booking)
                        .build()
                )
                .toList();
    }

    private List<Seat> findSeatAvailable(String flightCode, List<FlightBookingDetailRequestDTO> passengers) {
        Map<SeatType, Long> seatCountByType = passengers.stream()
                .collect(Collectors.groupingBy(
                        p -> SeatType.valueOf(p.getSeatType().toUpperCase()),
                        Collectors.counting()
                ));
        List<Seat> seats = seatCountByType.entrySet().stream()
                .flatMap(entry -> seatService.findSeatsAvailable(flightCode, entry.getKey(), entry.getValue().intValue()).stream())
                .toList();
        seatService.reserveSeat(seats);
        return seats;
    }

    @Override
    public Flight findByFlightCode(String flightCode) {
        return seatService.findByFlightCode(flightCode);
    }

    @Override
    public void releaseSeat(FlightBooking booking) {
        seatService.releaseSeat(booking.getFlightBookingDetails().stream()
                .map(FlightBookingDetail::getSeat)
                .toList());
    }

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


}
