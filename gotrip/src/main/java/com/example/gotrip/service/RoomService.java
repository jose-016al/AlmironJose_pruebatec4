package com.example.gotrip.service;

import com.example.gotrip.exception.HotelException;
import com.example.gotrip.model.Hotel;
import com.example.gotrip.model.Room;
import com.example.gotrip.repository.RoomRepository;
import com.example.gotrip.util.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomRepository repository;
    private final IHotelService hotelService;

    @Override
    public Room findRoomAvailable(String hotelCode, LocalDate checkIn, LocalDate checkOut, RoomType roomType) {
        Hotel hotel = hotelService.findByHotelCode(hotelCode);

        return hotel.getRooms().stream()
                .filter(room -> room.getRoomType().equals(roomType) &&
                        isRoomAvailable(room, checkIn, checkOut))
                .findFirst()
                .orElseThrow(() -> new HotelException("No hay habitaciones disponibles"));
    }

    @Override
    public double calculateTotalPrice(Room room, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return room.getPricePerNight() * nights;
    }

    private boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        return room.getBookings().stream().noneMatch(booking ->
                (booking.getCheckIn().isBefore(checkOut) && booking.getCheckOut().isAfter(checkIn))
        );
    }

}
