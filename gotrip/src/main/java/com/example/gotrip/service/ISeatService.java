package com.example.gotrip.service;

import com.example.gotrip.model.Flight;
import com.example.gotrip.model.Seat;
import com.example.gotrip.util.SeatType;

import java.util.List;

public interface ISeatService {
    public List<Seat> findSeatsAvailable(String flightCode, SeatType seatType, int numberOfSeats);
    public Flight findByFlightCode(String flightCode);
    public void reserveSeat(List<Seat> seats);
    public void releaseSeat(List<Seat> seats);
}
