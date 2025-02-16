package com.example.gotrip.service;

import com.example.gotrip.dto.FlightBookingDetailResponseDTO;
import com.example.gotrip.dto.FlightBookingRequestDTO;
import com.example.gotrip.model.Flight;
import com.example.gotrip.model.FlightBooking;
import com.example.gotrip.model.FlightBookingDetail;

import java.util.List;

public interface IFlightBookingDetailService {
    public List<FlightBookingDetail> generateDetails(FlightBookingRequestDTO request, FlightBooking booking);
    public Flight findByFlightCode(String flightCode);
    public void reserveSeat(FlightBooking booking);
    public void releaseSeat(FlightBooking booking);
    public List<FlightBookingDetailResponseDTO> getDetailsResponseDTOS(FlightBooking booking);
}
