package com.example.gotrip.service;

import com.example.gotrip.model.Hotel;
import com.example.gotrip.model.HotelBookingDetail;
import com.example.gotrip.model.Room;
import com.example.gotrip.util.RoomType;

import java.time.LocalDate;
import java.util.List;

public interface IRoomService {
    public List<Room> findRoomAvailable(String hotelCode, LocalDate checkIn, LocalDate checkOut, RoomType roomType, int numberOfRooms);
    public Hotel findByHotelCode(String hotelCode);
    public double calculateTotalPrice(List<HotelBookingDetail> bookingDetails, LocalDate checkIn, LocalDate checkOut);
}
