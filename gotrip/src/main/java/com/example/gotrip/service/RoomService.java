package com.example.gotrip.service;

import com.example.gotrip.exception.HotelException;
import com.example.gotrip.model.HotelBookingDetail;
import com.example.gotrip.model.Room;
import com.example.gotrip.repository.RoomRepository;
import com.example.gotrip.util.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomRepository repository;
    private final IHotelService hotelService;

    @Override
    public List<Room> findRoomAvailable(String hotelCode, LocalDate checkIn, LocalDate checkOut, RoomType roomType, int numberOfRooms) {
        List<Room> availableRooms = repository.findAvailableRooms(hotelCode, checkIn, checkOut, roomType);

        if (availableRooms.size() < numberOfRooms) {
            throw new HotelException("No hay suficientes habitaciones disponibles de tipo " + roomType);
        }

        return availableRooms;
    }

    @Override
    public double calculateTotalPrice(List<HotelBookingDetail> bookingDetails,
                                      LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return bookingDetails.stream()
                .mapToDouble(detail -> {
                    Room room = detail.getRoom();
                    return room != null ? room.getPricePerNight() * nights : 0;
                })
                .sum();
    }

}
