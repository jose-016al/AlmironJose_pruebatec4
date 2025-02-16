package com.example.gotrip.service;

import com.example.gotrip.dto.FlightBookingDetailResponseDTO;
import com.example.gotrip.dto.FlightBookingRequestDTO;
import com.example.gotrip.model.Flight;
import com.example.gotrip.model.FlightBooking;
import com.example.gotrip.model.FlightBookingDetail;
import com.example.gotrip.repository.FlightBookingDetailRepository;
import com.example.gotrip.util.SeatType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightBookingDetailService implements IFlightBookingDetailService {

    private final FlightBookingDetailRepository repository;
    private final ISeatService seatService;

    @Override
    public List<FlightBookingDetail> generateDetails(FlightBookingRequestDTO request, FlightBooking booking) {
        return request.getPassengers().stream()
                .map(detail -> FlightBookingDetail.builder()
                        .passengerName(detail.getPassengerName())
                        .passportNumber(detail.getPassportNumber())
                        .seat(seatService.findSeatAvailable(
                                request.getFlightCode(),
                                SeatType.valueOf(detail.getSeatType().toUpperCase())
                        ))
                        .flightBooking(booking)
                        .build()
                )
                .toList();
    }

    @Override
    public Flight findByFlightCode(String flightCode) {
        return seatService.findByFlightCode(flightCode);
    }

    @Override
    public void reserveSeat(FlightBooking booking) {
        booking.getFlightBookingDetails().stream()
                .map(FlightBookingDetail::getSeat)
                .forEach(seat -> seatService.reserveSeat(seat.getId()));
    }

    @Override
    public void releaseSeat(FlightBooking booking) {
        booking.getFlightBookingDetails().stream()
                .map(FlightBookingDetail::getSeat)
                .forEach(seat -> seatService.releaseSeat(seat.getId()));
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
