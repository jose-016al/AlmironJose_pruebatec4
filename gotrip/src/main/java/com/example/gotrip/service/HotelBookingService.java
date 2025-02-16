package com.example.gotrip.service;

import com.example.gotrip.dto.HotelBookingRequestDTO;
import com.example.gotrip.dto.HotelBookingResponseDTO;
import com.example.gotrip.exception.InvalidDateException;
import com.example.gotrip.exception.ResourceNotFoundException;
import com.example.gotrip.model.HotelBooking;
import com.example.gotrip.model.Room;
import com.example.gotrip.repository.HotelBookingRepository;
import com.example.gotrip.util.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelBookingService implements IHotelBookingService {

    private final HotelBookingRepository repository;
    private final IUserService userService;
    private final IRoomService roomService;

    @Override
    public HotelBookingResponseDTO save(HotelBookingRequestDTO request) {
        if (!request.getCheckOut().isAfter(request.getCheckIn())) {
            throw new InvalidDateException("La fecha de checkout debe ser posterior a la fecha de checkin.");
        }
        Room room = roomService.findRoomAvailable(
                request.getHotelCode(),
                request.getCheckIn(),
                request.getCheckOut(),
                RoomType.valueOf(request.getRoomType().toUpperCase())
        );
        HotelBooking booking = HotelBooking.builder()
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .totalPrice(roomService.calculateTotalPrice(room, request.getCheckIn(), request.getCheckOut()))
                .user(userService.findUser(request.getUser()))
                .room(room)
                .build();
        repository.save(booking);
        return buildResponseDTO(booking);
    }

    @Override
    public List<HotelBookingResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::buildResponseDTO)
                .toList();
    }

    @Override
    public HotelBookingResponseDTO findById(Long id) {
        return buildResponseDTO(findBookingOrThrow(id));
    }

    @Override
    public List<HotelBookingResponseDTO> findByHotelId(Long hotelId) {
        return repository.findByHotelId(hotelId).stream()
                .map(this::buildResponseDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        repository.delete(findBookingOrThrow(id));
    }

    private HotelBooking findBookingOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado la reserva con ID: " + id));
    }


    private HotelBookingResponseDTO buildResponseDTO(HotelBooking booking) {
        return HotelBookingResponseDTO.builder()
                .reserveId(booking.getId())
                .hotelCode(booking.getRoom().getHotel().getHotelCode())
                .checkIn(booking.getCheckIn())
                .checkOut(booking.getCheckOut())
                .roomType(booking.getRoom().getRoomType().name())
                .numberRoom(booking.getRoom().getNumber())
                .totalPrice(booking.getTotalPrice())
                .user(booking.getUser().getId())
                .build();
    }
}
