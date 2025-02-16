package com.example.gotrip.service;

import com.example.gotrip.model.Room;
import com.example.gotrip.util.RoomType;

import java.time.LocalDate;

public interface IRoomService {
    public Room findRoomAvailable(String hotelCode, LocalDate checkIn, LocalDate checkOut, RoomType roomType);
    public double calculateTotalPrice(Room room, LocalDate checkIn, LocalDate checkOut);
}
