package com.example.gotrip.service;

import com.example.gotrip.dto.FlightBookingRequestDTO;
import com.example.gotrip.dto.FlightBookingResponseDTO;

import java.util.List;

public interface IFlightBookingService {
    public FlightBookingResponseDTO save(FlightBookingRequestDTO request);
    public List<FlightBookingResponseDTO> findAll();
    public FlightBookingResponseDTO findById(Long id);
    public List<FlightBookingResponseDTO> findByFlightId(Long flightId);
    public void delete(Long id);
}
