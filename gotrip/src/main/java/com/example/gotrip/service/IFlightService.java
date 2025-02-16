package com.example.gotrip.service;

import com.example.gotrip.dto.FlightRequestDTO;
import com.example.gotrip.dto.FlightResponseDTO;
import com.example.gotrip.model.Flight;
import com.example.gotrip.model.Seat;
import com.example.gotrip.util.SeatType;

import java.time.LocalDate;
import java.util.List;

public interface IFlightService {
    public FlightResponseDTO save(FlightRequestDTO request);
    public List<FlightResponseDTO> findAll();
    public List<FlightResponseDTO> searchFlights(LocalDate dateFrom, LocalDate dateTo, String origin, String destination);
    public FlightResponseDTO findById(Long id);
    public Flight findByFlightCode(String flightCode);
    public FlightResponseDTO update(Long id, FlightRequestDTO request);
    public void delete(Long id);
}
