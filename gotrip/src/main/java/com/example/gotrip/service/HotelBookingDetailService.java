package com.example.gotrip.service;

import com.example.gotrip.dto.HotelBookingDetailRequestDTO;
import com.example.gotrip.dto.HotelBookingDetailResponseDTO;
import com.example.gotrip.dto.HotelBookingRequestDTO;
import com.example.gotrip.model.*;
import com.example.gotrip.repository.HotelBookingDetailRepository;
import com.example.gotrip.util.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelBookingDetailService implements IHotelBookingDetailService {

    private final HotelBookingDetailRepository repository;
    private final IRoomService roomService;

    /**
     * Genera las estancias de los huéspedes a partir de la solicitud de reserva.
     * @param request Datos de la solicitud de la reserva del hotel
     * @param booking Objeto de reserva del hotel
     * @return Lista de detalles de la reserva del hotel para cada huésped
     */
    @Override
    public List<HotelBookingDetail> generateStays(HotelBookingRequestDTO request, HotelBooking booking) {
        Map<RoomType, List<Room>> availableRoomsByType = findRoomAvailable(
                request.getHotelCode(),
                request.getCheckIn(),
                request.getCheckOut(),
                request.getGuests()
        );

        Map<RoomType, AtomicInteger> roomIndexByType = availableRoomsByType.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> new AtomicInteger(0)));

        return request.getGuests().stream()
                .map(guest -> {
                    RoomType type = RoomType.valueOf(guest.getRoomType().toUpperCase());
                    List<Room> rooms = availableRoomsByType.getOrDefault(type, List.of());
                    int index = roomIndexByType.get(type).getAndIncrement();
                    Room assignedRoom = index < rooms.size() ? rooms.get(index) : null;

                    return HotelBookingDetail.builder()
                            .guestName(guest.getGuestName())
                            .guestIdDocument(guest.getGuestIdDocument())
                            .room(assignedRoom)
                            .hotelBooking(booking)
                            .build();
                })
                .toList();
    }

    /**
     * Calcula el precio total de la reserva de hotel para los huéspedes.
     * @param bookingDetails Detalles de la reserva del hotel para cada huésped
     * @param checkIn Fecha de entrada
     * @param checkOut Fecha de salida
     * @return Precio total de la reserva
     */
    @Override
    public double calculateTotalPrice(List<HotelBookingDetail> bookingDetails, LocalDate checkIn, LocalDate checkOut) {
        return roomService.calculateTotalPrice(bookingDetails, checkIn, checkOut);
    }

    /**
     * Busca un hotel a partir de su código.
     * @param hotelCode Código del hotel
     * @return Objeto hotel correspondiente al código proporcionado
     */
    @Override
    public Hotel findByHotelCode(String hotelCode) {
        return roomService.findByHotelCode(hotelCode);
    }

    /**
     * Convierte los detalles de la reserva del hotel en un DTO de respuesta para cada huésped.
     * @param booking Objeto de reserva del hotel
     * @return Lista de DTOs con los detalles de la reserva de cada huésped
     */
    @Override
    public List<HotelBookingDetailResponseDTO> getStaysResponseDTOS(HotelBooking booking) {
        return booking.getHotelBookingDetails().stream()
                .map(guest -> HotelBookingDetailResponseDTO.builder()
                        .guestName(guest.getGuestName())
                        .guestIdDocument(guest.getGuestIdDocument())
                        .numberRoom(guest.getRoom().getNumber())
                        .roomType(guest.getRoom().getRoomType().name())
                        .build()
                )
                .toList();
    }

    /**
     * Busca las habitaciones disponibles por tipo para un hotel y unas fechas específicas.
     * @param hotelCode Código del hotel
     * @param checkIn Fecha de entrada
     * @param checkOut Fecha de salida
     * @param guests Lista de huéspedes con sus tipos de habitación
     * @return Mapa con las habitaciones disponibles por tipo de habitación
     */
    private Map<RoomType, List<Room>> findRoomAvailable(String hotelCode, LocalDate checkIn, LocalDate checkOut,
                                                        List<HotelBookingDetailRequestDTO> guests) {
        return guests.stream()
                .map(guest -> RoomType.valueOf(guest.getRoomType().toUpperCase()))
                .distinct()
                .collect(Collectors.toMap(
                        type -> type,
                        type -> roomService.findRoomAvailable(hotelCode, checkIn, checkOut, type,
                                (int) guests.stream().filter(g -> g.getRoomType().equalsIgnoreCase(type.name())).count())
                ));
    }
}
