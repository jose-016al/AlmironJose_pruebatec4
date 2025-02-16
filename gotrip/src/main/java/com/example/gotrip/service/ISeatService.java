package com.example.gotrip.service;

import com.example.gotrip.model.Flight;
import com.example.gotrip.model.Seat;
import com.example.gotrip.util.SeatType;

import java.util.List;

public interface ISeatService {
    public Seat findSeatAvailable(String flightCode, SeatType seatType);
    public Flight findByFlightCode(String flightCode);
    public void reserveSeat(Long id);
    public void releaseSeat(Long id);
}
